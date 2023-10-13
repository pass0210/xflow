package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.Message.Message;
import com.nhnacademy.aiot.port.Port;

public abstract class OutputNode extends ActiveNode{
    private final Port[] outputPorts;

    protected OutputNode(int outputCount) {
        outputPorts = new Port[outputCount];
    }

    public void connect(int index, Port inputPort) {
        outputPorts[index] = inputPort;
    }

    protected void output(int index, Message message) {
        synchronized (outputPorts[index]) {
            outputPorts[index].put(message);
            outputPorts[index].notifyAll();
        }
    }
}
