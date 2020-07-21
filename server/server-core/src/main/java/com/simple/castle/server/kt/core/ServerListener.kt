package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.simple.castle.server.game.ServerGame;
import com.simple.castle.server.user.UserWorker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerListener implements Runnable, Disposable {
    private static final int MAX_USERS = 4;

    private final ExecutorService userWorkersService = Executors.newFixedThreadPool(MAX_USERS);
    private final Set<UserWorker> workers = Collections.synchronizedSet(new HashSet<>());

    private final ServerGame serverGame;

    private final ServerSocketHints serverSocketHints = new ServerSocketHints();
    private final SocketHints socketHints = new SocketHints();
    private ServerSocket serverSocket = null;

    private boolean isRunning = false;

    public ServerListener(ServerGame serverGame) {
        this.serverGame = serverGame;
        this.serverSocketHints.acceptTimeout = 0;
    }

    @Override
    public void run() {
        Gdx.app.log("ServerListener", "Server going to wait for clients");

        isRunning = true;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints);

        while (isRunning && !Thread.currentThread().isInterrupted()) {
            Gdx.app.log("ServerListener", "Wait for next connection...");

            try {
                Socket socket = serverSocket.accept(socketHints);
                UserWorker worker = new UserWorker(socket, serverGame);
                workers.add(worker);
                userWorkersService.submit(worker);
            } catch (GdxRuntimeException exception) {
                Gdx.app.log("ServerListener", "Socket accept error");
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

}
