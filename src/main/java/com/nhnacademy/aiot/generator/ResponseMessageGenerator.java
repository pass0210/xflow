package com.nhnacademy.aiot.generator;

import java.net.Socket;

import com.nhnacademy.aiot.Header.ResponseHeader;
import com.nhnacademy.aiot.Message.ResponseMessage;
import com.nhnacademy.aiot.body.Body;

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
