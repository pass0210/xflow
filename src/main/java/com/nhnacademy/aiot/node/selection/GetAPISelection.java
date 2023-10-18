package com.nhnacademy.aiot.node.selection;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.header.RequestHeader;
import com.nhnacademy.aiot.node.InputOutputNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAPISelection extends InputOutputNode {

    public GetAPISelection(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                String api = takeApi(message);

                if (api.matches("/")) {
                    output(0, message);
                } else if (api.matches("\\/common\\.js")) {
                    output(1, message);
                } else if (api.matches("\\/temperature")) {
                    output(2, message);
                } else if (api.matches("\\/humidity")) {
                    output(3, message);
                } else if (api.matches("/dev")) {
                    output(4, message);
                } else if (api.matches("\\/dev(\\/[a-zA-Z0-9|\\-]+)?")) {
                    output(5, message);
                } else if (api.matches("\\/ep\\/\\w+\\/[a-zA-Z0-9|\\\\-]+(\\?.+=.+)?")) {
                    output(6, message);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private String takeApi(Message message) {
        RequestHeader header = (RequestHeader) message.getHeader();
        return header.getResource();
    }
}
