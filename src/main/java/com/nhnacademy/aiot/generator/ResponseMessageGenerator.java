package com.nhnacademy.aiot.generator;

import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;

@Slf4j
public class ResponseMessageGenerator {

    private ResponseHeader header;
    private Body body;

    public ResponseMessageGenerator(ResponseHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public ResponseMessage generate(Socket socket) {
        log.info("{}: 응답 메시지 생성", socket.getInetAddress());
        return new ResponseMessage(header, body, socket);
    }

}
