package com.nhnacademy.aiot.node.processor;

import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class DeviceAllInfoProcessor extends InputOutputNode {
    private static final String API_URL = "http://ems.nhnacademy.com:1880/dev";

    public DeviceAllInfoProcessor(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message requestMessage = tryGetMessage();
                log.info("[Client {}]: 메시지를 받음", requestMessage.getHeader().getId());

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

                        // body 생성
                        Body body = new Body(response.toString());

                        // Header 생성
                        ResponseHeader header = new ResponseHeader("200", "OK");
                        header.addHeader("Content-Type", "text/plain; charset=utf-8");
                        header.addHeader("Content-Length", String.valueOf(body.getData().length()));

                        // Message 만들기
                        ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(header, body);
                        ResponseMessage responseMessage = messageGenerator.generate(requestMessage.getSocket());

                        // output
                        output(0, responseMessage);
                    } else {
                        log.error("API 요청 실패. 응답 코드: {}" + responseCode);

                    }

                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
