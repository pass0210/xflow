package com.nhnacademy.aiot.node.filter;

import com.nhnacademy.aiot.message.RequestMessage;
import com.nhnacademy.aiot.checker.FormatChecker;
import com.nhnacademy.aiot.generator.RequestMessageGenerator;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.RequestHeader;
import com.nhnacademy.aiot.node.OutputNode;
import com.nhnacademy.aiot.splitter.RequestMessageSplitter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class FormatFilter extends OutputNode {

    public FormatFilter(int outputCount) {
        super(outputCount);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {

            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {

                        StringBuilder builder = new StringBuilder();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        // socket 메시지 전체 읽어서 저장
                        while (reader.ready()) {
                            builder.append((char) reader.read());
                        }

                        // 메시지 헤더, 바디 분리
                        RequestMessageSplitter messageSplitter = new RequestMessageSplitter();
                        String[] splitArray = messageSplitter.splitMessage(builder.toString());
                        String headerString = splitArray[0];
                        String bodyString = splitArray[1];

                        // 메시지 포맷 검사
                        FormatChecker formatChecker = new FormatChecker(headerString, bodyString);

                        // 0번 true, 1번 false
                        if (formatChecker.check()) {
                            // 메시지 생성
                            RequestMessageGenerator messageGenerator = new RequestMessageGenerator();
                            RequestMessage requestMessage = messageGenerator.generateMessage(headerString, bodyString,
                                    socket);

                            if (requestMessage == null) {
                                log.error("null");
                            }

                            output(0, requestMessage);
                        } else {
                            socket.close();
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error(e.getMessage());
                    }
                }).start();

            }
        } catch (IOException e) { // 서버 소켓 예외 처리
            log.error(e.getMessage());
        }
    }
}
