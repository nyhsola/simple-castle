package com.simple.castle.game.core.event;

public class EveryEvent {
    private final long every;
    private long nextCall;

    public EveryEvent(long every) {
        this.every = every;
    }

    public EveryEvent(long every, boolean autoStart) {
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
