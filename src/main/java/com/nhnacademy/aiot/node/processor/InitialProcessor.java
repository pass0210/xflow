package com.nhnacademy.aiot.node.processor;

import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class InitialProcessor extends InputOutputNode {

  public InitialProcessor(int inputCount, int outputCount) {
    super(inputCount, outputCount);
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Message requestMessage = tryGetMessage();
        log.info("{}: 메시지를 받음", requestMessage.getSocket().getInetAddress());

        File file = new File("www/index.html");
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
          // index.html 파일 읽어 body 담기
          StringBuilder contents = new StringBuilder();
          while (fileReader.ready()) {
            contents.append((char) fileReader.read());
          }
          Body body = new Body(contents.toString());

          // Header 만들기
          ResponseHeader header = new ResponseHeader("200", "OK");
          header.addHeader("Content-Type", "text/html; charset=utf-8");
          header.addHeader("Content-Length", String.valueOf(file.length()));

          // Message 만들기
          ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(header, body);
          ResponseMessage responseMessage = messageGenerator.generate(requestMessage.getSocket());

          // output
          output(0, responseMessage);

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