package com.nhnacademy.aiot.node.filter;

import java.util.List;
import java.util.Map;
import com.nhnacademy.aiot.message.header.RequestHeader;
import com.nhnacademy.aiot.message.header.ResponseHeader;
import com.nhnacademy.aiot.message.ExceptionMessage;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.ResponseMessage;
import com.nhnacademy.aiot.message.body.Body;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;
import com.nhnacademy.aiot.checker.APIChecker;
import com.nhnacademy.aiot.generator.HtmlGenerator;
import com.nhnacademy.aiot.generator.ResponseMessageGenerator;

@Slf4j
public class APIFilter extends InputOutputNode {
    private final Map<String, List<String>> map;

    public APIFilter(int inputCount, int outputCount, Map<String, List<String>> map) {
        super(inputCount, outputCount);
        this.map = map;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();

                RequestHeader header = (RequestHeader) message.getHeader();
                APIChecker apiChecker = new APIChecker(map, header);

                boolean flag = apiChecker.check();

                if (flag) {
                    output(0, message);
                } else {
                    ExceptionMessage exceptionMessage = new ExceptionMessage(header, new Body("Not Found"),
                            message.getSocket());

                    String htmlData = HtmlGenerator.generate("404 Not Found");
                    ResponseHeader responseHeader = new ResponseHeader("404", "Not Found");
                    responseHeader.addHeader("Content-Type", "text/html; charset=utf-8");
                    responseHeader.addHeader("Content-Length", String.valueOf(htmlData.length()));

                    // Message 만들기
                    ResponseMessageGenerator messageGenerator = new ResponseMessageGenerator(responseHeader,
                            new Body(htmlData));
                    ResponseMessage responseMessage = messageGenerator.generate(message.getSocket());
                    output(1, responseMessage);
                    output(1, exceptionMessage);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
