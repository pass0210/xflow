package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Port;

public abstract class InputNode extends ActiveNode{
    private final Port[] inputPorts;

    protected InputNode(int inputCount) {
        inputPorts = new Port[inputCount];

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

    protected void waitMessage() throws InterruptedException{
        synchronized (inputPorts[0]) {
            while (!inputPorts[0].hasMessage()) {
                inputPorts[0].wait();
            }
        }
    }
}
