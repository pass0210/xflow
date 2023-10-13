package com.nhnacademy.aiot.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.nhnacademy.aiot.Header.ResponseHeader;
import com.nhnacademy.aiot.Message.ExceptionMessage;
import com.nhnacademy.aiot.Message.Message;
import com.nhnacademy.aiot.Message.RequestMessage;
import com.nhnacademy.aiot.Message.ResponseMessage;
import com.nhnacademy.aiot.body.Body;
import com.nhnacademy.aiot.checker.FormatChecker;
import com.nhnacademy.aiot.generator.RequestMessageGenerator;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.node.OutputNode;
import com.nhnacademy.aiot.splitter.RequestMessageSplitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormatFilter extends OutputNode {
    private RequestMessageSplitter messageSplitter;
    private RequestMessageGenerator messageGenerator;

    protected FormatFilter(int outputCount) {
        super(outputCount);
        messageSplitter = new RequestMessageSplitter();
        messageGenerator = new RequestMessageGenerator();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {

            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                StringBuilder builder = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    // socket 메시지 전체 읽어서 저장
                    while (reader.ready()) {
                        builder.append((char) reader.read());
                    }

                    // 메시지 헤더, 바디 분리
                    String[] splitArray = messageSplitter.splitMessage(builder.toString());
                    String headerString = splitArray[0];
                    String bodyString = splitArray[1];

                    // 메시지 포맷 검사
                    FormatChecker formatChecker = new FormatChecker(headerString, bodyString);

                    // 0번 true, 1번 false
                    if (formatChecker.check()) {
                        // 메시지 생성
                        RequestMessage requestMessage = messageGenerator.generateMessage(headerString, bodyString);
                        System.out.println(requestMessage.getMessage());
                        // output(0, requestMessage);
                    } else {

                        // TODO
                        // 예외 메시지 생성
                        ExceptionMessage exceptionMessage = new ExceptionMessage(null, new Body("Exception TEST"));

                        ResponseHeader responseHeader = new ResponseHeader("404", "Not Found");
                        Body responseBody = new Body("Response TEST"); // HTML code 작성
                        ResponseMessageGenerator responseMessageGenerator = new ResponseMessageGenerator(responseHeader,
                                responseBody);
                        ResponseMessage responseMessage = responseMessageGenerator.generate();
                        // output(1, exceptionMessage);
                        // output(1, responseMessage);
                        System.out.println(exceptionMessage.getMessage());
                        System.out.println();
                        System.out.println(responseMessage.getMessage());
                    }
                }
            }
        } catch (IOException e) { // 서버 소켓 예외 처리
            log.error(e.getMessage());
        }
    }
}
