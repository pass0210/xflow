package com.nhnacademy.aiot.node.processor;

import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.RequestHeader;
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

@Slf4j
public class DeviceIdInfoProcessor extends InputOutputNode {
    private static final String API_BASE = "http://ems.nhnacademy.com:1880";

    public DeviceIdInfoProcessor(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                Socket socket = message.getSocket();
                String resource = ((RequestHeader) message.getHeader()).getResource();

                try {
                    // URL 객체 생성
                    URL url = new URL(API_BASE + resource);

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
                        JSONObject result = new JSONObject(response.toString());

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
