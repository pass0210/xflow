package com.nhnacademy.aiot.Message;

import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.body.Body;
import lombok.Getter;

@Getter
public abstract class Message {
    private final Header header;
    private final Body body;

    public Message(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    public abstract String getMessage();

    
}
