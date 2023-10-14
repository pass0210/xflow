package com.nhnacademy.aiot.methodSelection;

import com.nhnacademy.aiot.Message.Message;
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
                waitMessage();
                Message message = getInputPort(0).get();
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
        return message.getHeader().getFristHeaderLine().split(" ")[0];
    }
}
