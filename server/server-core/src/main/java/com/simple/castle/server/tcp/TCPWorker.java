package com.simple.castle.server.tcp;

import com.badlogic.gdx.net.Socket;
import com.simple.castle.base.World;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TCPWorker implements Runnable {

    private final Socket socket;

    private final BlockingDeque<World> deque = new LinkedBlockingDeque<>();

    public void add(World world) {
        deque.offer(world);
    }

    public TCPWorker(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while (socket.isConnected()) {
                World poll = deque.pollLast();
                if (poll != null) {
                    objectOutputStream.writeObject(poll);
                }
            }
        } catch (IOException exception) {
            System.out.println("IO exception");
        }
    }
}
