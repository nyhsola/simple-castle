package com.simple.castle.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.simple.castle.server.main.GameServer;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.mock;

public class Launcher {

    public static void main(String[] args) throws IOException {
        final Properties properties = new Properties();
        properties.load(Launcher.class.getResourceAsStream("/server.properties"));

        final GameServer listener = new GameServer();

        if (Boolean.parseBoolean(properties.getProperty("headless"))) {
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = mock(GL20.class);
            new HeadlessApplication(listener);
        } else {
            new LwjglApplication(listener);
        }
    }
}
