package com.nhnacademy.aiot.checker;

import java.util.stream.Stream;

public class FormatChecker implements Checker {
    private String header;
    private String body;

    public FormatChecker(String header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public boolean check() {
        String firstLine = header.lines().findFirst().get();
        String[] splitFirstLine = firstLine.split(" ");
        String method = splitFirstLine[0];
        String version = splitFirstLine[2];
        return methodCheck(method) && versionCheck(version) && headerMapCheck();
    }

    private boolean methodCheck(String method) {
        if (method.matches("^(?!GET$|POST$|DELETE$|PUT$).*")) {
            return false;
        }

        return true;
    }

    private boolean versionCheck(String version) {
        if (!version.equals("HTTP/1.1")) {
            return false;
        }

        return true;
    }

    private boolean headerMapCheck() {
        String[] lines = (String[]) header.lines().toArray();

        for (int i = 1; i < lines.length; i++) {
            if (!lines[i].matches("[a-zA-Z\\-]+: .+")) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

    }
}
