package com.nhnacademy.aiot.generator;

import java.net.Socket;

import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;

public class ResponseMessageGenerator {

    private ResponseHeader header;
    private Body body;

    public ResponseMessageGenerator(ResponseHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public ResponseMessage generate(Socket socket) {
        return new ResponseMessage(header, body, socket);
    }

}
