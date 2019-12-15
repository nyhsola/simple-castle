package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.simple.castle.scenes.MainScene;
import com.simple.castle.scenes.MenuScene;
import com.simple.castle.scenes.Scene;

import java.util.HashMap;
import java.util.Map;

public class Manager extends ApplicationAdapter implements ChangeScene {

    public static final String MAIN_SCENE = "MainScene";
    public static final String MENU_SCENE = "MenuScene";

    private static final String START_SCENE = MAIN_SCENE;

    private String currentScene;

    private Map<String, Scene> scenes = new HashMap<>();

    public Manager() {
        scenes.put(MAIN_SCENE, new MainScene(this));
        scenes.put(MENU_SCENE, new MenuScene(this));
    }

    @Override
    public void create() {
        this.setScene(START_SCENE);

        for (Scene scene : scenes.values()) {
            scene.create();
        }
    }

    @Override
    public void render() {
        if (scenes.containsKey(currentScene)) {
            scenes.get(currentScene).render();
        }
    }

    @Override
    public void dispose() {
        for (Scene scene : scenes.values()) {
            scene.dispose();
        }
    }

    @Override
    public void setScene(String scene) {
        if (scenes.containsKey(scene)) {
            Gdx.input.setInputProcessor(scenes.get(scene));
            currentScene = scene;
        }
    }

}
