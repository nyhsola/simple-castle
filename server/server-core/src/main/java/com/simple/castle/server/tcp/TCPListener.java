package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.simple.castle.base.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TCPListener implements Runnable, GameDataListener {
    private static final int MAX_USERS = 16;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_USERS);
    private final ExecutorService copy = Executors.newFixedThreadPool(1);

    private final ServerSocketHints serverSocketHints = new ServerSocketHints();
    private final List<TCPWorker> workers = Collections.synchronizedList(new ArrayList<>());

    public TCPListener() {
        serverSocketHints.acceptTimeout = 5000;
    }

    @Override
    public void run() {
        System.out.println("Listner started");
        ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints);

        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept(null);
            TCPWorker worker = new TCPWorker(socket);
            workers.add(worker);
            service.submit(worker);
        }

        try {
            System.out.println("Waiting for workers shutdown");
            if (!service.awaitTermination(2, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

    @Override
    public void newData(World world) {
        World newWorld = new World(world);
        copy.submit(() -> {
            for (TCPWorker worker : workers) {
                worker.add(new World(newWorld));
            }
        });
    }

}
