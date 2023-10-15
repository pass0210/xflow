package com.nhnacademy.aiot.message;

import java.net.Socket;

import com.nhnacademy.aiot.message.header.Header;
import com.nhnacademy.aiot.message.body.Body;

public class RequestMessage extends Message {

    public RequestMessage(Header header, Body body, Socket socket) {
        super(header, body, socket);
    }

    @Override
    public String getMessage() {
        return getHeader().toString() + System.lineSeparator() + getBody().getData();
    }

}
