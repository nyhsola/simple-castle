package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.simple.castle.scenes.MainScene;
import com.simple.castle.scenes.Scene;

import java.util.HashMap;
import java.util.Map;

public class Game extends ApplicationAdapter {

    public static final String MAIN_SCENE = "MainScene";

    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private Manager manager;

    @Override
    public void create() {
        Map<String, Scene> sceneMap = new HashMap<>();
        sceneMap.put(MAIN_SCENE, new MainScene());

        manager = new Manager(MAIN_SCENE, sceneMap);
        manager.create();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        manager.render();
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

}
