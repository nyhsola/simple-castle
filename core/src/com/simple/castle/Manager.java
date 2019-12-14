package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;

import java.util.ArrayList;
import java.util.List;

public class Manager extends ApplicationAdapter {

    private List<ApplicationAdapter> scenes = new ArrayList<>();
    private int currentScene = 0;

    public Manager() {
        scenes.add(new MainScene());
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

}
