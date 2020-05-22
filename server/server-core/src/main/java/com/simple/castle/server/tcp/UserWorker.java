package com.simple.castle.server.tcp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.base.ModelSend;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class UserWorker implements Runnable, Disposable {
    private final Socket socket;
    private final BlockingDeque<List<ModelSend>> deque = new LinkedBlockingDeque<>();

    public UserWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Gdx.app.log("UserListener", "User connected, starting transfer data");

        try {
            if (!socket.isConnected()) {
                Gdx.app.log("UserListener", "User disconnected");
                return;
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                List<ModelSend> poll = deque.pollLast();
                if (poll != null) {
                    objectOutputStream.writeObject(poll);
                }
            }
        } catch (IOException exception) {
            Gdx.app.log("UserWorker", exception.getMessage());
        }
    }

    public void addWorldTick(List<ModelSend> modelSendList) {
        deque.offerFirst(modelSendList);
    }

    @Override
    public void dispose() {
        socket.dispose();
    }
}
