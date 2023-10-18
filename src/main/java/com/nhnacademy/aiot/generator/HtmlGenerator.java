package com.nhnacademy.aiot.generator;

public class HtmlGenerator {
    public static String generate(String message) {
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
