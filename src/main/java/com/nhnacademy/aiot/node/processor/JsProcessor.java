package com.nhnacademy.aiot.node.processor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.node.InputOutputNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsProcessor extends InputOutputNode {

    public JsProcessor(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waitMessage();

                Message requestMessage = getInputPort(0).get();

                File file = new File("www/common.js");
                try (FileReader reader = new FileReader(file)) {
                    // index.html 파일 읽어 body 담기
                    StringBuilder contents = new StringBuilder();
                    while (reader.ready()) {
                        contents.append((char) reader.read());
                    }
                    Body body = new Body(contents.toString());

                    // Header 만들기
                    ResponseHeader header = new ResponseHeader("200", "OK");
                    header.addHeader("Content-Type", "application/javascript; charset=UTF-8");
                    header.addHeader("Content-Length", String.valueOf(contents.length()));

                    // Message 만들기
                    ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(header, body);
                    ResponseMessage responseMessage = messageGenerator.generate(requestMessage.getSocket());

                    // output
                    output(0, responseMessage);

                } catch (IOException e) {
                    log.error(e.getMessage());
                }

            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

    }

}