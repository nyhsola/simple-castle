package com.simple.castle.server.tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerStarter {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ServerListener serverListener;

    public ServerStarter(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public void start() {
        executor.submit(serverListener);
    }

    public void stop() {
        System.out.println("Waiting for shutdown listener");
        try {
            serverListener.dispose();
            System.out.println("Disposed");

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }

            System.out.println("Done");
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

}
