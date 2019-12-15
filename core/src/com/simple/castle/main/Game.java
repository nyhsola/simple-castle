package com.simple.castle.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.simple.castle.manager.impl.Manager;
import com.simple.castle.manager.impl.Scene;
import com.simple.castle.scenes.MainScene;

import java.util.HashMap;
import java.util.Map;

public class Game extends ApplicationAdapter implements InputProcessor {

    public static final String MAIN_SCENE = "MainScene";

    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private Manager gameManager;

    @Override
    public void create() {
        Map<String, Scene> sceneMap = new HashMap<>();
        sceneMap.put(MAIN_SCENE, new MainScene());

        gameManager = new Manager(MAIN_SCENE, sceneMap);
        gameManager.create();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
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

    @Override
    public boolean keyDown(int keycode) {
        return gameManager.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return gameManager.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return gameManager.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return gameManager.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return gameManager.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return gameManager.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return gameManager.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return gameManager.scrolled(amount);
    }
}
