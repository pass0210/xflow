package com.nhnacademy.aiot.message.header;

import lombok.Getter;

@Getter
public class RequestHeader extends Header {

    private final String method;
    private final String resource;

    public RequestHeader(String method, String resource) {
        this.method = method;
        this.resource = resource;
    }

    @Override
    public String getFristHeaderLine() {
        return method + " " + resource + " " + HTTP_VERSION;
    }
}
