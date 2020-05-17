package com.simple.castle.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

public class Launcher {
    public static void main(String[] args) {
        new HeadlessApplication(new CastleGame());
        Gdx.app.log("Server", "Started!");

        new Thread(() -> {
            ServerSocketHints serverSocketHints = new ServerSocketHints();
            serverSocketHints.acceptTimeout = 0;
            ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints);
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept(null);
            }
        }).start();
    }
}
