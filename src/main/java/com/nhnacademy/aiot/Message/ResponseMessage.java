package com.nhnacademy.aiot.Message;

import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.body.Body;

public class ResponseMessage extends Message {
    public ResponseMessage(Header header, Body body) {
        super(header, body);
    }

    @Override
    public String getMessage() {
        return getHeader().toString() + System.lineSeparator() + getBody().getData();
    }
}
