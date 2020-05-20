package com.simple.castle.server;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.simple.castle.server.main.SimpleCastleGame;
import com.simple.castle.server.tcp.ServerListener;
import com.simple.castle.server.tcp.ServerStarter;

import java.util.Scanner;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class ServerLauncher {

    public static void main(String[] args) {
        final Application application;

        final ServerListener serverListener = new ServerListener();
        final SimpleCastleGame game = new SimpleCastleGame(serverListener);
        final ServerStarter serverStarter = new ServerStarter(serverListener);

        if (isGUI(args)) {
            application = new LwjglApplication(game);
        } else {
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = mock(GL20.class);
            Gdx.gl30 = mock(GL30.class);
            application = new HeadlessApplication(game);
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

        serverStarter.start();

        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        while (!"stop".equals(next)) {
            next = scanner.next();
        }

        Gdx.app.exit();
    }

    private static boolean isGUI(String[] args) {
        return args.length >= 1 && Stream.of(args).noneMatch(arg -> arg.contains("--no-gui"));
    }
}
