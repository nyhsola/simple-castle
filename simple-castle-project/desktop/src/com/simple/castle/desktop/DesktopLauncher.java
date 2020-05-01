package com.simple.castle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.simple.castle.main.GameLauncher;

import java.util.stream.Stream;

public class DesktopLauncher {
    public static void main(String[] arg) {
//		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Stream.of(arg)
                .forEach(value -> {
                    if (value.contains("height")) {
                        config.height = Integer.parseInt(value.split("=")[1]);
                    }
                    if (value.contains("width")) {
                        config.width = Integer.parseInt(value.split("=")[1]);
                    }
                });
        new LwjglApplication(new GameLauncher(), config);
    }
}
