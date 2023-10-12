package com.nhnacademy.aiot.Message;

import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.body.Body;

public class ExceptionMessage extends Message {
    

    public ExceptionMessage(Header header, Body body) {
        super(header, body);
    }

    @Override
    public String getMessage() {
        return getBody().getData();
    }

    
}
