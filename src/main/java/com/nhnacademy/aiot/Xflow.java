package com.nhnacademy.aiot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhnacademy.aiot.apiselection.GetAPISelection;
import com.nhnacademy.aiot.filter.APIFilter;
import com.nhnacademy.aiot.filter.FormatFilter;
import com.nhnacademy.aiot.methodSelection.MethodSelection;
import com.nhnacademy.aiot.processor.HumidityProcessor;
import com.nhnacademy.aiot.processor.InitialProcessor;
import com.nhnacademy.aiot.processor.JsProcessor;
import com.nhnacademy.aiot.processor.TemperatureProcessor;
import com.nhnacademy.aiot.response.HttpResponseNode;
import com.nhnacademy.aiot.response.ResponseSender;

public class Xflow {
    private static Map<String, List<String>> apiMap = new HashMap<>();

    public static void main(String[] args) {
        initAPIMap();

        FormatFilter formatFilter = new FormatFilter(2);
        APIFilter apiFilter = new APIFilter(1, 2, apiMap);

        MethodSelection methodSelection = new MethodSelection(1, 4);
        GetAPISelection getAPISelection = new GetAPISelection(1, 8);

        InitialProcessor initialProcessor = new InitialProcessor(1, 1);
        JsProcessor jsProcessor = new JsProcessor(1, 1);
        TemperatureProcessor temperatureProcessor = new TemperatureProcessor(1, 1);
        HumidityProcessor humidityProcessor = new HumidityProcessor(1, 1);

        HttpResponseNode httpResponseNode = new HttpResponseNode(1, 2);
        ResponseSender responseSender = new ResponseSender(1);

        formatFilter.connect(0, apiFilter.getInputPort(0));
        apiFilter.connect(0, methodSelection.getInputPort(0));

        methodSelection.connect(0, getAPISelection.getInputPort(0));
        getAPISelection.connect(0, initialProcessor.getInputPort(0));
        getAPISelection.connect(5, jsProcessor.getInputPort(0));
        getAPISelection.connect(6, temperatureProcessor.getInputPort(0));
        getAPISelection.connect(7, humidityProcessor.getInputPort(0));

        initialProcessor.connect(0, httpResponseNode.getInputPort(0));
        jsProcessor.connect(0, httpResponseNode.getInputPort(0));
        temperatureProcessor.connect(0, httpResponseNode.getInputPort(0));
        humidityProcessor.connect(0, httpResponseNode.getInputPort(0));

        httpResponseNode.connect(0, responseSender.getInputPort(0));

        formatFilter.start();
        apiFilter.start();

        methodSelection.start();
        getAPISelection.start();

        initialProcessor.start();
        jsProcessor.start();
        temperatureProcessor.start();
        humidityProcessor.start();

        httpResponseNode.start();
        responseSender.start();
    }

    private static void initAPIMap() {
        List<String> resourceList = new ArrayList<>();

        resourceList.add("/");
        resourceList.add("/dev");
        resourceList.add("\\/dev(\\/[a-zA-Z0-9|\\-]+)?");
        resourceList.add("/ep");
        resourceList.add("\\/ep\\/\\w+\\/[a-zA-Z0-9|\\\\-]+(\\?.+)?");
        resourceList.add("\\/common\\.js");
        resourceList.add("\\/temperature");
        resourceList.add("\\/humidity");

        apiMap.put("GET", resourceList);
    }
}
