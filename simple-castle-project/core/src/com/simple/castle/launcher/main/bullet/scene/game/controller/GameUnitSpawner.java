package com.simple.castle.launcher.main.bullet.scene.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.simple.castle.launcher.main.bullet.scene.game.GameScene;

public class GameUnitSpawner extends InputAdapter {

    private final GameScene gameLauncher;

    public GameUnitSpawner(GameScene gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (Input.Keys.SPACE == keycode) {
//            gameLauncher.spawn();
        }

        return super.keyDown(keycode);
    }
}
