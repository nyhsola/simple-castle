package com.simple.castle;

import com.badlogic.gdx.Gdx;
import com.simple.castle.scenes.Scene;

import java.util.Map;

public class Manager extends Scene implements ChangeScene {

    private String startScene;
    private String currentScene;

    private Map<String, Scene> sceneMap;

    public Manager(String startScene, Map<String, Scene> sceneMap) {
        this.sceneMap = sceneMap;
        this.startScene = startScene;

        for (Scene scene : this.sceneMap.values()) {
            scene.setChangeScene(this);
        }
    }

    @Override
    public void create() {
        this.setScene(startScene);

        for (Scene scene : sceneMap.values()) {
            scene.create();
        }
    }

    @Override
    public void render() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).render();
        }
    }

    @Override
    public void dispose() {
        for (Scene scene : sceneMap.values()) {
            scene.dispose();
        }
    }

    @Override
    public void setScene(String scene) {
        if (sceneMap.containsKey(scene)) {
            Gdx.input.setInputProcessor(sceneMap.get(scene));
            currentScene = scene;
        }
    }

}
