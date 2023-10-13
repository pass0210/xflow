package com.nhnacademy.aiot.node;

public abstract class ActiveNode implements Runnable{
    private final Thread thread;

    protected ActiveNode() {
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}
