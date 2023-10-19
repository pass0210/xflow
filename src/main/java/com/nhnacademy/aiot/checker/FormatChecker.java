package com.nhnacademy.aiot.checker;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class FormatChecker implements Checker {
    private String header;
    private String body;

    public FormatChecker(String header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public boolean check() {
        log.info("Format 체크 접근");
        String firstLine = header.lines().findFirst().get();
        String[] splitFirstLine = firstLine.split(" ");
        String method = splitFirstLine[0];
        String version = splitFirstLine[2];

        return methodCheck(method) && versionCheck(version) && headerMapCheck();
    }

    private boolean methodCheck(String method) {
        return method.matches("^(?!GET$|POST$|DELETE$|PUT$).*");
    }

    private boolean versionCheck(String version) {
        return !version.equals("HTTP/1.1");
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
