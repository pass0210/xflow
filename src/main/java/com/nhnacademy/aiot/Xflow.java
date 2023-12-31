package com.nhnacademy.aiot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhnacademy.aiot.node.selection.GetAPISelection;
import com.nhnacademy.aiot.node.filter.APIFilter;
import com.nhnacademy.aiot.node.filter.FormatFilter;
import com.nhnacademy.aiot.node.selection.MethodSelection;
import com.nhnacademy.aiot.node.trace.TraceNode;
import com.nhnacademy.aiot.node.processor.DeviceAllInfoProcessor;
import com.nhnacademy.aiot.node.processor.DeviceIdInfoProcessor;
import com.nhnacademy.aiot.node.processor.HumidityProcessor;
import com.nhnacademy.aiot.node.processor.InitialProcessor;
import com.nhnacademy.aiot.node.processor.JsProcessor;
import com.nhnacademy.aiot.node.processor.SensorTypeInfoProcessor;
import com.nhnacademy.aiot.node.processor.TemperatureProcessor;
import com.nhnacademy.aiot.node.response.HttpResponseNode;
import com.nhnacademy.aiot.node.response.ResponseSender;

public class Xflow {
    private static final Map<String, List<String>> API_MAP = new HashMap<>();

    public static void main(String[] args) {
        initAPIMap();

        // node init
        FormatFilter formatFilter = new FormatFilter(2);
        APIFilter apiFilter = new APIFilter(1, 2, API_MAP);

        MethodSelection methodSelection = new MethodSelection(1, 5);
        GetAPISelection getAPISelection = new GetAPISelection(1, 7);

        InitialProcessor initialProcessor = new InitialProcessor(1, 1);
        JsProcessor jsProcessor = new JsProcessor(1, 1);
        TemperatureProcessor temperatureProcessor = new TemperatureProcessor(1, 1);
        HumidityProcessor humidityProcessor = new HumidityProcessor(1, 1);
        DeviceAllInfoProcessor deviceAllInfoProcessor = new DeviceAllInfoProcessor(1, 1);
        DeviceIdInfoProcessor deviceIdInfoProcessor = new DeviceIdInfoProcessor(1, 1);
        SensorTypeInfoProcessor sensorTypeInfoProcessor = new SensorTypeInfoProcessor(1, 1);

        HttpResponseNode httpResponseNode = new HttpResponseNode(1, 2);
        ResponseSender responseSender = new ResponseSender(1);

        TraceNode traceNode = new TraceNode(1);

        // node connection
        formatFilter.connect(0, apiFilter.getInputPort(0));
        formatFilter.connect(1, httpResponseNode.getInputPort(0));
        apiFilter.connect(0, methodSelection.getInputPort(0));
        apiFilter.connect(1, httpResponseNode.getInputPort(0));

        methodSelection.connect(0, getAPISelection.getInputPort(0));
        getAPISelection.connect(0, initialProcessor.getInputPort(0));
        getAPISelection.connect(1, jsProcessor.getInputPort(0));
        getAPISelection.connect(2, temperatureProcessor.getInputPort(0));
        getAPISelection.connect(3, humidityProcessor.getInputPort(0));
        getAPISelection.connect(4, deviceAllInfoProcessor.getInputPort(0));
        getAPISelection.connect(5, deviceIdInfoProcessor.getInputPort(0));
        getAPISelection.connect(6, sensorTypeInfoProcessor.getInputPort(0));

        initialProcessor.connect(0, httpResponseNode.getInputPort(0));
        jsProcessor.connect(0, httpResponseNode.getInputPort(0));
        temperatureProcessor.connect(0, httpResponseNode.getInputPort(0));
        humidityProcessor.connect(0, httpResponseNode.getInputPort(0));
        deviceAllInfoProcessor.connect(0, httpResponseNode.getInputPort(0));
        deviceIdInfoProcessor.connect(0, httpResponseNode.getInputPort(0));
        sensorTypeInfoProcessor.connect(0, httpResponseNode.getInputPort(0));

        httpResponseNode.connect(0, responseSender.getInputPort(0));
        httpResponseNode.connect(1, traceNode.getInputPort(0));

        // node start
        formatFilter.start();
        apiFilter.start();

        methodSelection.start();
        getAPISelection.start();

        initialProcessor.start();
        jsProcessor.start();
        temperatureProcessor.start();
        humidityProcessor.start();
        deviceAllInfoProcessor.start();
        deviceIdInfoProcessor.start();
        sensorTypeInfoProcessor.start();

        httpResponseNode.start();
        responseSender.start();
        traceNode.start();
    }

    private static void initAPIMap() {
        List<String> resourceList = new ArrayList<>();

        resourceList.add("/");
        resourceList.add("\\/common\\.js");
        resourceList.add("\\/temperature");
        resourceList.add("\\/humidity");
        resourceList.add("/dev");
        resourceList.add("\\/dev(\\/[a-zA-Z0-9|\\-]+)?");
        resourceList.add("\\/ep\\/\\w+\\/[a-zA-Z0-9|\\\\-]+(\\?(.+=.+))?");

        API_MAP.put("GET", resourceList);
    }
}
