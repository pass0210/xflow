package com.nhnacademy.aiot.message.header;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;

public abstract class Header {
    protected static final String HTTP_VERSION = "HTTP/1.1"; // final에서는 값을 고정해야하니 주소 값을 지정해준다.
    @Getter
    private final String id;
    private final Map<String, String> headerMap = new HashMap<>();
    private static final AtomicLong count = new AtomicLong(0);

    protected Header() {
        id = String.format("%s-%02d", getClass().getSimpleName(), count.incrementAndGet());
    }

    public void addHeader(String key, String value) {
        headerMap.put(key, value); // 키 값 지정
    }

    public abstract String getFristHeaderLine();

    public Object getHeaderMap() {
        return null;
    }

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
