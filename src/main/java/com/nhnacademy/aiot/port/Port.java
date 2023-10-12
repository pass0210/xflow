package com.nhnacademy.aiot.port;

import java.util.LinkedList;
import java.util.Queue;

public class Port {
    Queue<Messaage> messageQueue;

    public Port() {
        messageQueue = new LinkedList<>();
    }

    public void put(Message messsage) {
        messageQueue.add(message);
    }

    public boolean hasMessage() {
        return !messageQueue.isEmpty();
    }

    public Message get() {
        return messageQueue.poll();
    }

    public void remove() {
        messageQueue.remove();
    }
}
