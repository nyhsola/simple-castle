package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;

import java.util.ArrayList;
import java.util.List;

public class Manager extends ApplicationAdapter implements ChangeScene {

    private List<ApplicationAdapter> scenes = new ArrayList<>();
    private int currentScene = 0;

    public Manager() {
        scenes.add(new MainScene(this));
        scenes.add(new MenuScene(this));
    }

    @Override
    public void create() {
        for (ApplicationAdapter adapter : scenes) {
            adapter.create();
        }
    }

    @Override
    public void render() {
        scenes.get(currentScene).render();
    }

    @Override
    public void dispose() {
        for (ApplicationAdapter adapter : scenes) {
            adapter.dispose();
        }
    }

    @Override
    public void setScene(int scene) {
        if (scene >= 0 && scene < scenes.size() && scenes.get(scene) != null) {
            currentScene = scene;
        }
    }

}
