package com.nhnacademy.aiot.filter;


import java.util.List;
import java.util.Map;
import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.Header.ResponseHeader;
import com.nhnacademy.aiot.Message.ExceptionMessage;
import com.nhnacademy.aiot.Message.Message;
import com.nhnacademy.aiot.Message.ResponseMessage;
import com.nhnacademy.aiot.body.Body;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;
import com.nhnacademy.aiot.checker.APIChecker;

@Slf4j
public class APIFilter extends InputOutputNode {
    private ResponseMessageGenerator responseMessageGenerator;
    private Map<String, List<String>> map;

    protected APIFilter(int inputCount, int outputCount, Map<String, List<String>> map) {
        super(inputCount, outputCount);
        this.map = map;
    }

    @Override
    public void run() {
        try {
            waitMessage();
            Message message = getInputPort(0).get();
            Header header = message.getHeader();
            APIChecker apiChecker = new APIChecker(map, header);

            boolean flag = apiChecker.check();

            if (flag) {
                output(0, message);
            } else {
                ExceptionMessage exceptionMessage =
                        new ExceptionMessage(null, new Body("Exception TEST"), message.getSocket());

                ResponseHeader responseHeader = new ResponseHeader("404", "Not Found");
                Body responseBody = new Body("Response TEST"); // HTML code 작성
                ResponseMessageGenerator responseMessageGenerator =
                        new ResponseMessageGenerator(responseHeader, responseBody);
                ResponseMessage responseMessage =
                        responseMessageGenerator.generate(message.getSocket());
                output(1, exceptionMessage);
                output(1, responseMessage);
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }


}
