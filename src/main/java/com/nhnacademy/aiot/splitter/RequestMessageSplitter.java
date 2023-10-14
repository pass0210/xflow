package com.nhnacademy.aiot.splitter;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class RequestMessageSplitter {
    public String[] splitMessage(String message) {
        StringBuilder headerBuilder = new StringBuilder();
        StringBuilder bodyBuilder = new StringBuilder();

        Iterator<String> iter = message.lines().iterator();

        String line;
        while (iter.hasNext() && !(line = iter.next()).isEmpty()) {
            headerBuilder.append(line).append(System.lineSeparator());
        }

        while (iter.hasNext()) {
            bodyBuilder.append(iter.next()).append(System.lineSeparator());
        }

        String headerString = headerBuilder.toString();
        String bodyString = bodyBuilder.toString();

        return new String[] { headerString, bodyString };
    }
}