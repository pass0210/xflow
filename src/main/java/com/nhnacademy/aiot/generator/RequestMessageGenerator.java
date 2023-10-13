package com.nhnacademy.aiot.generator;

import java.util.Iterator;

import com.nhnacademy.aiot.Header.RequestHeader;
import com.nhnacademy.aiot.Message.RequestMessage;
import com.nhnacademy.aiot.body.Body;

public class RequestMessageGenerator {
    public RequestMessage generateMessage(String header, String body) {
        RequestHeader requestHeader = generateHeader(header);
        Body requestBody = generateBody(body);

        return new RequestMessage(requestHeader, requestBody);
    }

    private RequestHeader generateHeader(String header) {
        Iterator<String> headerIterator = header.lines().iterator();

        String firstLine = headerIterator.next();
        String[] firstLineSplit = firstLine.split(" ");
        String method = firstLineSplit[0];
        String resource = firstLineSplit[1];

        RequestHeader requestHeader = new RequestHeader(method, resource);

        while (headerIterator.hasNext()) {
            String line = headerIterator.next();
            String[] splitString = line.split(": ");
            requestHeader.addHeader(splitString[0], splitString[1]);
        }

        return requestHeader;
    }

    private Body generateBody(String body) {
        return new Body(body);
    }
}