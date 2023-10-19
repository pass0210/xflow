package com.nhnacademy.aiot.generator;

import com.nhnacademy.aiot.message.RequestMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.RequestHeader;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.Iterator;

@Slf4j
public class RequestMessageGenerator {
    public RequestMessage generateMessage(String header, String body, Socket socket) {
        log.info("{}: 요청 메시지 생성", socket.getInetAddress());
        RequestHeader requestHeader = generateHeader(header);
        Body requestBody = generateBody(body);

        return new RequestMessage(requestHeader, requestBody, socket);
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