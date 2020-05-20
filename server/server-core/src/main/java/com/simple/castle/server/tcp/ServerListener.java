package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
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
    private boolean isRunning = false;

    public ServerListener() {
    }

    @Override
    public void run() {
        isRunning = true;

        Gdx.app.log("ServerListener", "Server going to wait for clients");
        final ServerSocketHints serverSocketHints = new ServerSocketHints();
        serverSocketHints.acceptTimeout = 0;

        SocketHints socketHints = new SocketHints();

        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints);

        while (isRunning && !Thread.currentThread().isInterrupted()) {
            Gdx.app.log("ServerListener", "Wait for next connection...");

            Socket socket = null;
            try {
                socket = serverSocket.accept(socketHints);
            } catch (GdxRuntimeException exception) {
                Gdx.app.log("ServerListener", "Socket accept error");
            }

            if (socket != null && socket.isConnected()) {
                UserWorker worker = new UserWorker(socket);

                workers.add(worker);
                userWorkersService.submit(worker);
            }
        }

        Gdx.app.log("ServerListener", "End of listening");
    }

    public void stop() {
        isRunning = false;
        try {
            Gdx.app.log("ServerListener", "Server going to shutdown user workers");
            userWorkersService.shutdown();
            if (!userWorkersService.awaitTermination(1, TimeUnit.SECONDS)) {
                userWorkersService.shutdownNow();
            }

            dataSubmitterService.shutdown();
            if (!dataSubmitterService.awaitTermination(1, TimeUnit.SECONDS)) {
                dataSubmitterService.shutdownNow();
            }

            for (UserWorker userWorker : workers) {
                userWorker.dispose();
            }
        } catch (InterruptedException e) {
            Gdx.app.error("ServerListener", "Interrupted: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        if (serverSocket != null) {
            Gdx.app.log("ServerListener", "Server socket disposed");
            serverSocket.dispose();
        }
    }

    @Override
    public void worldTick(World world) {
        if (isRunning) {
            World newWorld = new World(world);
            dataSubmitterService.submit(() -> {
                for (UserWorker worker : workers) {
                    worker.addWorldTick(new World(newWorld));
                }
            });
        }
    }
}
