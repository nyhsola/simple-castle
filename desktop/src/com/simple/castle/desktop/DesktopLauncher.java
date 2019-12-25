package com.simple.castle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.simple.castle.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
//		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 800;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}
