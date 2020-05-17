package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TCPListener implements Runnable {
    private static final int MAX_USERS = 16;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_USERS);

    @Override
    public void run() {
        ServerSocketHints serverSocketHints = new ServerSocketHints();
        serverSocketHints.acceptTimeout = 0;
        ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints);

        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept(null);
            service.submit(new TCPWorker(socket));
        }

        try {
            if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

}
