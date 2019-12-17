package com.simple.castle.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.simple.castle.constants.Scenes;
import com.simple.castle.manager.Manager;
import com.simple.castle.scenes.MainScene;

public class Game extends ApplicationAdapter {

    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);
    private Manager gameManager;

    @Override
    public void create() {
        gameManager = new Manager.ManagerBuilder()
                .addScene(Scenes.MAIN_SCENE, new MainScene())
                .build();
        gameManager.create();
        Gdx.input.setInputProcessor(gameManager);
    }

    @Override
    public void render() {
        gameManager.update();
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        gameManager.render();
    }

    @Override
    public void resize(int width, int height) {
        gameManager.resize(width, height);
    }

    @Override
    public void pause() {
        gameManager.pause();
    }

    @Override
    public void resume() {
        gameManager.resume();
    }

    @Override
    public void dispose() {
        gameManager.dispose();
    }

}
