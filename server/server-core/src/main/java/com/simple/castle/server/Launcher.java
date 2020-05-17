package com.simple.castle.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.simple.castle.server.main.GameServer;
import com.simple.castle.server.tcp.TCPListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        new HeadlessApplication(new GameServer());
        executorService.submit(new TCPListener());

        Gdx.app.log("Server", "Started!");

        // TODO: 5/17/2020 Correctly stop server
    }
}
