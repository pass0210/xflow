package com.nhnacademy.aiot.generator;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class HtmlGenerator {

    private HtmlGenerator() {
    }

    public static String generate(String message) {
        File file = new File(HtmlGenerator.class.getClassLoader().getResource("www/exception.html").getFile());

        StringBuilder contents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            while (reader.ready()) {
                contents.append(reader.readLine());
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return contents.toString().replaceAll("\\{MESSAGE\\}", message);
    }
}
