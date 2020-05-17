package com.simple.castle.server.tcp;

import com.badlogic.gdx.net.Socket;

public class TCPWorker implements Runnable {

    private final Socket socket;

    public TCPWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
