package com.nhnacademy.aiot.message;

import java.net.Socket;

import com.nhnacademy.aiot.message.header.Header;
import com.nhnacademy.aiot.message.body.Body;

public class ExceptionMessage extends Message {
    public ExceptionMessage(Header header, Body body, Socket socket) {
        super(header, body, socket);
    }

    @Override
    public String getMessage() {
        return getBody().getData();
    }

}
