package com.nhnacademy.aiot.node.processor;

import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TemperatureProcessor extends InputOutputNode {
    private static final String API_URL = "http://ems.nhnacademy.com:1880/dev/24e124128c067999";

    public TemperatureProcessor(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                log.info("[Client {}]: 메시지를 받음", message.getHeader().getId());
                Socket socket = message.getSocket();

                try {
                    // URL 객체 생성
                    URL url = new URL(API_URL);

                    // HttpURLConnection 객체 생성
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // GET 요청 설정
                    connection.setRequestMethod("GET");

                    // 응답 코드 확인
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 응답 데이터를 읽어옴
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // 응답 출력
                        JSONObject jsonObject = new JSONObject(response.toString());
                        double temperature = jsonObject.getJSONObject("id").getJSONObject("status")
                                .getJSONObject("sensor").getDouble("temperature");
                        LocalDateTime now = LocalDateTime.now();

                        JSONObject result = new JSONObject();
                        result.put("dateTime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        result.put("temperature", temperature);

                        // 바디 생성
                        Body body = new Body(result.toString());

                        // 헤더 생성
                        ResponseHeader header = new ResponseHeader("200", "OK");
                        header.addHeader("Content-Type", "application/json; charset=UTF-8");
                        header.addHeader("Content-Length", String.valueOf(body.getData().length()));

                        // 메시지 생성
                        ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(header, body);
                        ResponseMessage responseMessage = messageGenerator.generate(socket);

                        // output
                        output(0, responseMessage);
                    } else {
                        log.error("API 요청 실패. 응답 코드: {}" + responseCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}