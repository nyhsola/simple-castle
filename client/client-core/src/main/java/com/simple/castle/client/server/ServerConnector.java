package com.simple.castle.client.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.base.ServerRespond;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerConnector implements Disposable {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServerReader serverReader;

    public ServerConnector(String host, int port) {
        serverReader = new ServerReader(host, port);
        executorService.submit(serverReader);
    }

    public ServerRespond getNextWorldTick(long waitMillis) {
        return serverReader.getNextWorld(waitMillis);
    }

    @Override
    public void dispose() {
        Gdx.app.log("ServerConnector", "Server reader going to shutdown");

        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Gdx.app.log("ServerConnector", e.getMessage());
        }
    }
}
