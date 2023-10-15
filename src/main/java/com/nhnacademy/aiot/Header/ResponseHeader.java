package com.nhnacademy.aiot.Header;

import org.json.HTTP;
import lombok.Getter;

@Getter
public class ResponseHeader extends Header  {
    private final String statusCode;
    private final String statusMessage;

    public ResponseHeader(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    @Override
    public String getFristHeaderLine() {
        return HTTP_VERSION + " " + statusCode + " " + statusMessage;
        
    }
}

