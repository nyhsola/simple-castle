package com.simple.castle.server.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.simple.castle.server.game.ServerGame;
import com.simple.castle.server.tcp.ServerListener;
import com.simple.castle.server.tcp.ServerStarter;

import java.util.Scanner;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class ServerLauncher {

    public static void main(String[] args) {
        final Application application;

        final boolean isGUI = isGUI(args);
        final boolean isServer = isServer(args);

        final ServerGame game = new ServerGame(isGUI);
        final ServerStarter serverStarter = new ServerStarter(new ServerListener(game));

        if (isGUI) {
            application = new LwjglApplication(game);
        } else {
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = mock(GL20.class);
            Gdx.gl30 = mock(GL30.class);
            application = new HeadlessApplication(game);
        }

        if (isServer) {
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

        if (!isGUI) {
            Scanner scanner = new Scanner(System.in);
            String next;
            do {
                next = scanner.hasNext() ? scanner.next() : "";
            } while (!"stop".equals(next));
            Gdx.app.postRunnable(() -> Gdx.app.exit());
        }
        Gdx.app.log("ServerLauncher", "Main thread done");
    }

    private static boolean isGUI(String[] args) {
        return args.length == 0 || Stream.of(args).noneMatch(arg -> arg.contains("--no-gui"));
    }

    private static boolean isServer(String[] args) {
        return args.length != 0 && Stream.of(args).anyMatch(arg -> arg.contains("--server"));
    }
}
