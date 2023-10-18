package com.nhnacademy.aiot.node.response;

import com.nhnacademy.aiot.message.ExceptionMessage;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpResponseNode extends InputOutputNode {
    public HttpResponseNode(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();

                if (message instanceof ResponseMessage) {
                    output(0, message);
                } else if (message instanceof ExceptionMessage) {
                    output(1, message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
