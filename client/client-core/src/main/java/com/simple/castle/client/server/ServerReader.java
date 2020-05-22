package com.simple.castle.client.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.simple.castle.base.ModelSend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class ServerReader implements Runnable {
    private final Socket socket;
    private final ObjectInputStream inputStream;

    private final BlockingDeque<List<ModelSend>> fetchQueue = new LinkedBlockingDeque<>();

    public ServerReader(String host, int port) {
        SocketHints socketHints = new SocketHints();

        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, socketHints);

        ObjectInputStream socketStream;
        try {
            socketStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException exception) {
            socketStream = null;
        }

        inputStream = socketStream;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            fetchQueue.offerFirst(readFromServer());
        }
    }

    public List<ModelSend> getNextWorld(long waitMillis) {
        try {
            return fetchQueue.pollLast(waitMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Gdx.app.log("ServerReader", "Interrupt during waiting for next world");
        }
        return null;
    }

    private List<ModelSend> readFromServer() {
        List<ModelSend> world;
        try {
            world = (List<ModelSend>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            world = null;
        }
        return world;
    }
}
