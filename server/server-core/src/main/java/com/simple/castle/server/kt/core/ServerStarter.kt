package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerStarter {
    private final ExecutorService serverListenerService = Executors.newSingleThreadExecutor();
    private final ServerListener serverListener;

    public ServerStarter(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public void start() {
        serverListenerService.submit(serverListener);
    }

    public void stop() {
        Gdx.app.log("ServerStarter", "Waiting for shutdown listener");
        try {
            serverListener.stop();
            serverListener.dispose();
            serverListenerService.shutdown();
            if (!serverListenerService.awaitTermination(5, TimeUnit.SECONDS)) {
                serverListenerService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Gdx.app.log("ServerStarter", e.getMessage());
        }
        Gdx.app.log("ServerStarter", "Server was stopped");
    }

}
