package com.nhnacademy.aiot.node.response;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.node.InputNode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

@Slf4j
public class ResponseSender extends InputNode {

    public ResponseSender(int inputCount) {
        super(inputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                log.info("[Client {}]: 메시지를 받음", message.getHeader().getId());

                Socket socket = message.getSocket();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write(message.getMessage());
                writer.flush();
                writer.close();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
