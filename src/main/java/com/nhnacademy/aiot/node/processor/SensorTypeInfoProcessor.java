package com.nhnacademy.aiot.node.processor;

import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.RequestHeader;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

@Slf4j
public class SensorTypeInfoProcessor extends InputOutputNode {

    public SensorTypeInfoProcessor(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                log.info("{}: 메시지를 받음", message.getSocket().getInetAddress());
                Socket socket = message.getSocket();
                String apiUrl = "http://ems.nhnacademy.com:1880" + ((RequestHeader) message.getHeader()).getResource();

                try {
                    // URL 객체를 생성
                    URL url = new URL(apiUrl);

                    // httpURLConeection 객체를 생성
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // get 요청설정
                    connection.setRequestMethod("GET");

                    // 응답코드확인
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 응답 데이터를 읽어옴.
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // 바디 생성
                        Body body = new Body(response.toString());

                        // 헤더 생성
                        ResponseHeader header = new ResponseHeader("200", "OK");
                        header.addHeader("Content-Type", "application/json; charset=UTF-8");
                        header.addHeader("Content-Length", String.valueOf(body.getData().length()));

                        // 메세지 생성
                        ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(header, body);
                        ResponseMessage responseMessage = messageGenerator.generate(socket);

                        // output
                        output(0, responseMessage);
                    } else {
                        log.error("API 요청 실패. 응답 코드: {}", responseCode);
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
