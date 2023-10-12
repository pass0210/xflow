package com.nhnacademy.aiot.Message;

import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.body.Body;

public abstract class Message {
    private final Header header;
    private final Body body;

    public Message(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    public String getMessage() {
        return generateHeaderFomat() + System.lineSeparator() + generateHeaderFomat();
    }

    private String generateHeaderFomat() {
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(header.getFristHeaderLine());
        headerBuilder.append(System.lineSeparator());

        for (String key : header.getHeaderMap().keySet()) {
            headerBuilder.append(key).append(" : ").append(header.getHeaderMap().get(key));
        }   headerBuilder.append(System.lineSeparator());
        return headerBuilder.toString();
    }
    
    private String generatorBodyFormat() {
        return body.getData();
    }

    
}
