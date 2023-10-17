package com.nhnacademy.aiot.node.selection;

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

@Slf4j
public class MethodSelection extends InputOutputNode {

    public MethodSelection(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                String method = takeMethod(message);

                if (method.equalsIgnoreCase("GET")) {
                    output(0, message);
                } else if (method.equalsIgnoreCase("POST")) {
                    output(1, message);
                } else if (method.equalsIgnoreCase("PUT")) {
                    output(2, message);
                } else if (method.equalsIgnoreCase("DELETE")) {
                    output(3, message);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private String takeMethod(Message message) {
        RequestHeader header = (RequestHeader) message.getHeader();
        return header.getMethod();
    }
}
