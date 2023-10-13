package com.nhnacademy.aiot.Message;

import java.net.Socket;

import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.body.Body;

public class ResponseMessage extends Message {

    public ResponseMessage(Header header, Body body, Socket socket) {
        super(header, body, socket);
    }

    @Override
    public String getMessage() {
        return getHeader().toString() + System.lineSeparator() + getBody().getData();
    }

}
