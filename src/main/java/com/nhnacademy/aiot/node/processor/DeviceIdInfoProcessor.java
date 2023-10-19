package com.nhnacademy.aiot.node.processor;

import com.nhnacademy.aiot.generator.HtmlGenerator;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.message.ExceptionMessage;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.RequestHeader;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
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
                log.info("{}: 메시지를 받음", message.getSocket().getInetAddress());
                Socket socket = message.getSocket();
                RequestHeader requestHeader = (RequestHeader) message.getHeader();
                String resource = requestHeader.getResource();

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
                        ResponseHeader responseHeader = new ResponseHeader("200", "OK");
                        requestHeader.addHeader("Content-Type", "application/json; charset=UTF-8");
                        requestHeader.addHeader("Content-Length", String.valueOf(body.getData().length()));

                        // 메시지 생성
                        ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(responseHeader, body);
                        ResponseMessage responseMessage = messageGenerator.generate(socket);

                        // output
                        output(0, responseMessage);
                    } else {
                        log.error("API 요청 실패. 응답 코드: {}" + responseCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    ExceptionMessage exceptionMessage = new ExceptionMessage(requestHeader, new Body("Not Found Device"),
                            message.getSocket());

                    String htmlData = HtmlGenerator.generate("404 Not Found");
                    ResponseHeader responseHeader = new ResponseHeader("404", "Not Found");
                    responseHeader.addHeader("Content-Type", "text/html; charset=utf-8");
                    responseHeader.addHeader("Content-Length", String.valueOf(htmlData.length()));

                    // Message 만들기
                    ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(responseHeader,
                            new Body(htmlData));
                    ResponseMessage responseMessage = messageGenerator.generate(message.getSocket());
                    output(0, responseMessage);
                    output(0, exceptionMessage);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
