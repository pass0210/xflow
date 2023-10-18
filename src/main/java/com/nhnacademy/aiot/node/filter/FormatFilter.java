package com.nhnacademy.aiot.node.filter;

import com.nhnacademy.aiot.message.ExceptionMessage;
import com.nhnacademy.aiot.message.RequestMessage;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.checker.FormatChecker;
import com.nhnacademy.aiot.generator.HtmlGenerator;
import com.nhnacademy.aiot.generator.RequestMessageGenerator;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
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
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                        StringBuilder builder = new StringBuilder();

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

                        if (builder.toString().isEmpty()) {
                            socket.close();
                            return;
                        }

                        // 0번 true, 1번 false
                        if (formatChecker.check()) {
                            // 메시지 생성
                            RequestMessageGenerator messageGenerator = new RequestMessageGenerator();
                            RequestMessage requestMessage = messageGenerator.generateMessage(headerString, bodyString,
                                    socket);

                            output(0, requestMessage);
                        } else {
                            RequestMessageGenerator requestMessageGenerator = new RequestMessageGenerator();
                            RequestMessage requestMessage = requestMessageGenerator.generateMessage(headerString,
                                    bodyString,
                                    socket);

                            ExceptionMessage exceptionMessage = new ExceptionMessage(requestMessage.getHeader(),
                                    new Body("Format Error"),
                                    requestMessage.getSocket());

                            String htmlData = HtmlGenerator.generate("404 Not Found");
                            ResponseHeader responseHeader = new ResponseHeader("404", "Format Error");
                            responseHeader.addHeader("Content-Type", "text/html; charset=utf-8");
                            responseHeader.addHeader("Content-Length", String.valueOf(htmlData.length()));

                            // Message 만들기
                            ResponseMessageGenerator responseMessageGenerator = new ResponseMessageGenerator(
                                    responseHeader,
                                    new Body(htmlData));
                            ResponseMessage responseMessage = responseMessageGenerator
                                    .generate(requestMessage.getSocket());
                            output(1, responseMessage);
                            output(1, exceptionMessage);
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
