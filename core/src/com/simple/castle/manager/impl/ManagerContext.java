package com.simple.castle.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerContext {

    private final Map<String, String> settings = new HashMap<>();

    private final Map<String, Scene> sceneMap;
    private final List<String> alwaysRender;
    private final List<String> blockInput;
    private String currentScene;

    public ManagerContext(Map<String, Scene> sceneMap, List<String> alwaysRender, List<String> blockInput) {
        this.currentScene = sceneMap.keySet().stream().findFirst().orElse("NO_SCENE");
        this.sceneMap = sceneMap;
        this.alwaysRender = alwaysRender;
        this.blockInput = blockInput;
    }

    public void putSettings(String name, String value) {
        sceneMap.values().forEach(scene -> scene.settingUpdated(name, value));
        settings.put(name, value);
    }

    public String getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
    }

    public Map<String, Scene> getSceneMap() {
        return sceneMap;
    }

    public List<String> getAlwaysRender() {
        return alwaysRender;
    }

    public List<String> getBlockInput() {
        return blockInput;
    }

}
