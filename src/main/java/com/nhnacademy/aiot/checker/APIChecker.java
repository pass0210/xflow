package com.nhnacademy.aiot.checker;

import com.nhnacademy.aiot.message.header.Header;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class APIChecker implements Checker {
    private Map<String, List<String>> headerMap;
    private Header header;

    public APIChecker(Map<String, List<String>> headerMap, Header header) {
        this.headerMap = headerMap;
        this.header = header;
    }

    @Override
    public boolean check() {
        log.info("{}: API 체크 접근", header.getFristHeaderLine());
        String[] splitString = header.getFristHeaderLine().split(" ");
        String method = splitString[0];
        String api = splitString[1];
        boolean flag = false;

        for (String pattern : headerMap.get(method)) {
            if (api.matches(pattern)) {
                flag = true;
            }
        }

        return flag;
    }
}
