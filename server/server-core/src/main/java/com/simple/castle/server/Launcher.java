package com.simple.castle.server;

import com.badlogic.gdx.backends.headless.HeadlessApplication;

public class Launcher {
    public static void main(String[] args) {
        new HeadlessApplication(new CastleGame());
    }
}
