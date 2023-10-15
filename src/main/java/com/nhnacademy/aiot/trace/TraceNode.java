package com.nhnacademy.aiot.trace;

import com.nhnacademy.aiot.Message.Message;
import com.nhnacademy.aiot.node.InputNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceNode extends InputNode {
    public TraceNode(int inputCount) {
        super(inputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waitMessage();

                Message message = getInputPort(0).get();

                log.error(message.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
