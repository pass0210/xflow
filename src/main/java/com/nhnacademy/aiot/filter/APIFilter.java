package com.nhnacademy.aiot.filter;

import java.util.List;
import java.util.Map;
import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.Message.Message;
import com.nhnacademy.aiot.node.InputOutputNode;
import lombok.extern.slf4j.Slf4j;
import com.nhnacademy.aiot.checker.APIChecker;

@Slf4j
public class APIFilter extends InputOutputNode {
    // TODO
    // run()문 안에 추가
    // private ResponseMessageGenerator responseMessageGenerator;

    private final Map<String, List<String>> map;

    public APIFilter(int inputCount, int outputCount, Map<String, List<String>> map) {
        super(inputCount, outputCount);
        this.map = map;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waitMessage();
                Message message = getInputPort(0).get();
                Header header = message.getHeader();
                APIChecker apiChecker = new APIChecker(map, header);

                boolean flag = apiChecker.check();

                if (flag) {
                    output(0, message);
                } else {
                    log.error("exception message error");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage());
            }
        }
    }
}
