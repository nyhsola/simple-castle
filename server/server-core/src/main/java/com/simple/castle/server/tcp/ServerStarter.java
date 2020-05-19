package com.simple.castle.server.tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerStarter {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final TCPListener task;

    public ServerStarter(TCPListener task) {
        this.task = task;
    }

    public void start() {
        executor.submit(task);
    }

    public void stop() {
        System.out.println("Waiting for shutdown listener");
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

}
