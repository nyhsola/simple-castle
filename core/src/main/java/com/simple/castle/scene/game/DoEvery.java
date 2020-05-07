package com.simple.castle.scene.game;

public class DoEvery {
    private final long every;
    private long nextCall;

    public DoEvery(long every) {
        this.every = every;
    }

    public DoEvery(long every, boolean autoStart) {
        this.every = every;
        if (autoStart) start();
    }

    public void start() {
        nextCall = System.currentTimeMillis() + every;
    }

    public void update(Runnable runnable) {
        if (nextCall <= System.currentTimeMillis()) {
            runnable.run();
            start();
        }
    }

    public long nextCallIn() {
        return nextCall - System.currentTimeMillis();
    }
}
