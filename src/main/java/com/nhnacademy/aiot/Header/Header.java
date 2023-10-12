package com.nhnacademy.aiot.Header;

import java.util.HashMap;
import java.util.Map;

public abstract class Header {
    protected static final String HTTP_VERSION = "HTTP/1.1"; // final에서는 값을 고정해야하니 주소 값을 지정해준다.
    private final Map<String, String> headerMap = new HashMap<>();

    public void addHeader(String key, String value) {
        headerMap.put(key, value); // 키 값 지정
    }

    public abstract String getFristHeaderLine();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(getFristHeaderLine() + System.lineSeparator());
        for (String key : headerMap.keySet()) {
            result.append(key + ": " + headerMap.get(key) + System.lineSeparator());
        }
        return result.toString();
    }
}
