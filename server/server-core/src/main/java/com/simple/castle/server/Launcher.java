package com.simple.castle.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.simple.castle.server.game.core.main.GameServer;
import com.simple.castle.server.tcp.TCPListener;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Launcher.class.getResourceAsStream("/server.properties"));

        String type = properties.getProperty("type");

        if ("server".equals(type)) {
            new HeadlessApplication(new GameServer());
            executorService.submit(new TCPListener());
            Gdx.app.log("Server", "Started!");
        }

        if ("app".equals(type)) {
            new LwjglApplication(new GameServer());
        }

        // TODO: 5/17/2020 Correctly stop server
    }
}
