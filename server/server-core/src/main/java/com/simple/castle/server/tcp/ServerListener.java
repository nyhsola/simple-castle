package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.base.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerListener implements Runnable, DataListener, Disposable {
    private static final int MAX_USERS = 4;

    private final ExecutorService userWorkersService = Executors.newFixedThreadPool(MAX_USERS);
    private final ExecutorService dataSubmitterService = Executors.newSingleThreadExecutor();

    private final List<UserWorker> workers = Collections.synchronizedList(new ArrayList<>());

    private ServerSocket serverSocket = null;

    public ServerListener() {
    }

    @Override
    public void run() {
        Gdx.app.log("ServerListener", "Server going to wait for clients");
        final ServerSocketHints serverSocketHints = new ServerSocketHints();
        serverSocketHints.acceptTimeout = 0;

        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints);

        while (!Thread.currentThread().isInterrupted()) {
            Gdx.app.log("ServerListener", "Wait for next connection...");
            Socket socket = serverSocket.accept(null);

            if (socket.isConnected()) {
                UserWorker worker = new UserWorker(socket);

                workers.add(worker);
                userWorkersService.submit(worker);
            }
        }

        try {
            Gdx.app.log("ServerListener", "Server going to shutdown user workers");
            if (!userWorkersService.awaitTermination(1, TimeUnit.SECONDS)) {
                userWorkersService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Gdx.app.error("ServerListener", e.getMessage());
        }
    }

    @Override
    public void worldTick(World world) {
        World newWorld = new World(world);
        dataSubmitterService.submit(() -> {
            for (UserWorker worker : workers) {
                worker.addWorldTick(new World(newWorld));
            }
        });
    }

    @Override
    public void dispose() {
        if (serverSocket != null) {
            serverSocket.dispose();
        }
    }
}
