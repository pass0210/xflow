package com.nhnacademy.aiot.generator;

public class HtmlGenerator {
    public static String generate(String message) {
        StringBuilder result = new StringBuilder();
        result.append(
                "<!DOCTYPE html></br><html lang=\"ko\"></br><head></br><script src=\"https://cdn.jsdelivr.net/npm/@tabler/core@latest/dist/js/tabler.min.js\" defer></script></br><link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@tabler/core@latest/dist/css/tabler.min.css\"></br><link rel=\"icon\" href=\"data:,\"></br><script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script></br></head></br><body>");
        result.append("</br>" + message + "</br>");
        result.append("</body></br></html>");

        return result.toString();
    }
}
