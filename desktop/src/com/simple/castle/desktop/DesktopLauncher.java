package com.simple.castle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.simple.castle.launcher.main.bullet.BGameLauncher;

public class DesktopLauncher {
	public static void main (String[] arg) {
//		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        new LwjglApplication(new BGameLauncher(), config);
    }
}
