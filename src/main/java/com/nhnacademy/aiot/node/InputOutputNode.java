package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.port.Port;

public abstract class InputOutputNode extends ActiveNode{
    private final Port[] inputPorts;
    private final Port[] outputPorts;

    protected InputOutputNode(int inputCount, int outputCount) {
        inputPorts = new Port[inputCount];
        outputPorts = new Port[outputCount];

        for (int i = 0; i < inputCount; i++) {
            inputPorts[i] = new Port();
        }
    }

    public Port getInputPort(int index) {
        return inputPorts[index];
    }

    public int getInputPortCount() {
        return inputPorts.length;
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

    protected void waitMessage() throws InterruptedException{
        synchronized (inputPorts[0]) {
            while (!inputPorts[0].hasMessage()) {
                inputPorts[0].wait();
            }
        }
    }
}
