package com.simple.castle.launcher.main.bullet.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.simple.castle.launcher.main.bullet.main.GameLauncher;

public class GameObjectController extends InputAdapter {

    private final GameLauncher gameLauncher;

    public GameObjectController(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (Input.Keys.SPACE == keycode) {
            gameLauncher.spawn();
        }

        return super.keyDown(keycode);
    }
}
