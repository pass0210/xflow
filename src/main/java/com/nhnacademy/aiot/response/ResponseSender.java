package com.nhnacademy.aiot.response;

import com.nhnacademy.aiot.Message.Message;
import com.nhnacademy.aiot.node.InputNode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

@Slf4j
public class ResponseSender extends InputNode {

    protected ResponseSender(int inputCount) {
        super(inputCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waitMessage();

                Message message = getInputPort(0).get();

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
