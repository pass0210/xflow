package com.nhnacademy.aiot.node.trace;

import com.nhnacademy.aiot.message.ExceptionMessage;
import com.nhnacademy.aiot.message.header.RequestHeader;
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
                ExceptionMessage message = (ExceptionMessage) tryGetMessage();
                log.info("[Client {}]: 메시지를 받음", message.getHeader().getId());

                String ip = message.getSocket().getInetAddress().toString();
                String method = ((RequestHeader) message.getHeader()).getMethod();
                String resource = ((RequestHeader) message.getHeader()).getResource();
                String exceptionString = message.getMessage();

                log.error("[{}] [{} {}] [{}]", ip, method, resource, exceptionString);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
