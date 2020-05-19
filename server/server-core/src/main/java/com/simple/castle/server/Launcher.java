package com.simple.castle.server;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.simple.castle.server.main.GameServer;
import com.simple.castle.server.tcp.ServerStarter;
import com.simple.castle.server.tcp.TCPListener;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.mock;

public class Launcher {

    public static void main(String[] args) throws IOException {
        final Properties properties = new Properties();
        properties.load(Launcher.class.getResourceAsStream("/server.properties"));

        final Application application;

        final TCPListener tcpListener = new TCPListener();
        final GameServer listener = new GameServer(tcpListener);
        final ServerStarter serverStarter = new ServerStarter(tcpListener);
        final boolean isHeadless = Boolean.parseBoolean(properties.getProperty("headless"));

        if (isHeadless) {
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = mock(GL20.class);
            application = new HeadlessApplication(listener);
            serverStarter.start();
        } else {
            application = new LwjglApplication(listener);
            serverStarter.start();
        }

        application.addLifecycleListener(new LifecycleListener() {
            @Override
            public void pause() {

            }

            @Override
            public void resume() {

            }

            @Override
            public void dispose() {
                serverStarter.stop();
            }
        });
    }
}
