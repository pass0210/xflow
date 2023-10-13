package com.nhnacademy.aiot.checker;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.nhnacademy.aiot.Header.Header;
import com.nhnacademy.aiot.Header.RequestHeader;

public class APIChecker implements Checker {
    private Map<String, List<String>> headerMap;
    private Header header;

    public APIChecker(Map<String, List<String>> headerMap, Header header) {
        this.headerMap = headerMap = headerMap;
        this.header = header;
    }

    @Override
    public boolean check() {
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

    public static void main(String[] args) {
        Map<String, List<String>> headerMap = new HashMap<>();
        List<String> apis = new ArrayList<>();
        apis.add("/");
        apis.add("/dev");
        apis.add("/dev/.+");
        apis.add("/ep");
        apis.add("/ep/\\w+/[a-zA-Z0-9|-]+(\\?.+)?");
        headerMap.put("GET", apis);
        Header header = new RequestHeader("GET", "/ep/temporate/asdlkfja?sdafhaksdjf");

        APIChecker apiChecker = new APIChecker(headerMap, header);
        System.out.println(apiChecker.check());
    }
}
