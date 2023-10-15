package com.nhnacademy.aiot.apiselection;

import com.nhnacademy.aiot.Message.Message;
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
                waitMessage();

                Message message = getInputPort(0).get();
                String api = takeApi(message);

                if (api.matches("/")) {
                    output(0, message);
                } else if (api.matches("/dev")) {
                    output(1, message);
                } else if (api.matches("\\/dev(\\/[a-zA-Z0-9|\\-]+)?")) {
                    output(2, message);
                } else if (api.matches("/ep")) {
                    output(3, message);
                } else if (api.matches("\\/ep\\/\\w+\\/[a-zA-Z0-9|\\\\-]+(\\?.+)?")) {
                    output(4, message);
                } else if (api.matches("\\/common\\.js")) {
                    output(5, message);
                } else if (api.matches("\\/temperature")) {
                    output(6, message);
                } else if (api.matches("\\/humidity")) {
                    output(7, message);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private String takeApi(Message message) {
        return message.getHeader().getFristHeaderLine().split(" ")[1];
    }
}
