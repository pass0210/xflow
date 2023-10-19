package com.nhnacademy.aiot.generator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HtmlGenerator {

    private HtmlGenerator() {
    }

    public static String generate(String message) {
        log.info("Exception Html 생성 접근");

        return "<!DOCTYPE html>" +
                "<html lang=\"ko\">" +
                "<head>" +
                "<script src=\"https://cdn.jsdelivr.net/npm/@tabler/core@latest/dist/js/tabler.min.js\" defer></script>"
                +
                "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@tabler/core@latest/dist/css/tabler.min.css\">"
                +
                "<link rel=\"icon\" href=\"data:,\">" +
                "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>" +
                "</head>" +
                "<body>" +
                message +
                "</body>" +
                "</html>";
    }
}
