package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.simple.castle.launcher.main.bullet.scene.GameScene;

public class GameLauncher implements ApplicationListener {

    private GameScene gameScene;

    @Override
    public void create() {
        gameScene = new GameScene();
        gameScene.create();

        Gdx.input.setInputProcessor(gameScene);
    }

    @Override
    public void resize(int width, int height) {
        gameScene.resize(width, height);
    }

    @Override
    public void render() {
        gameScene.render();
    }

    @Override
    public void pause() {
        gameScene.pause();
    }

    @Override
    public void resume() {
        gameScene.resume();
    }

    @Override
    public void dispose() {
        gameScene.dispose();
    }
}