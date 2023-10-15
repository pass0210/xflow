package com.nhnacademy.aiot.message;

import java.net.Socket;

import com.nhnacademy.aiot.message.header.Header;
import com.nhnacademy.aiot.message.body.Body;
import lombok.Getter;

@Getter
public abstract class Message {
    private final Header header;
    private final Body body;
    private final Socket socket;

    public Message(Header header, Body body, Socket socket) {
        this.header = header;
        this.body = body;
        this.socket = socket;
    }

    public abstract String getMessage();

}
