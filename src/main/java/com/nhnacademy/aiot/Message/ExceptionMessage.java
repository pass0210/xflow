package com.nhnacademy.aiot.Message;

import java.net.Socket;

import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.body.Body;

public class ExceptionMessage extends Message {
    public ExceptionMessage(Header header, Body body, Socket socket) {
        super(header, body, socket);
    }

    @Override
    public String getMessage() {
        return getBody().getData();
    }

}
