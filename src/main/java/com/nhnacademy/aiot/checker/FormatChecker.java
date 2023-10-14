package com.nhnacademy.aiot.checker;

import java.util.Iterator;

public class FormatChecker implements Checker {
    private String header;
    private String body;

    public FormatChecker(String header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public boolean check() {
        if (header.isEmpty()) return true;

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
        boolean flag = false;
        Iterator<String> iter = header.lines().iterator();

        while (iter.hasNext()) {
            if (!flag) {
                flag = true;
                iter.next();
                continue;
            }
            if (!iter.next().matches("[a-zA-Z\\-]+: .+")) {
                return false;
            }
        }

        return true;
    }
}
