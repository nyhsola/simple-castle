package com.simple.castle.manager;

import com.simple.castle.scene.Scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerContext {

    private final Map<String, String> settings = new HashMap<>();

    private final Map<String, Scene> sceneMap;
    private final List<String> alwaysRender;
    private final List<String> blockInput;
    private String currentScene;

    public ManagerContext(Map<String, Scene> sceneMap, List<String> alwaysRender, List<String> blockInput, String currentScene) {
        this.currentScene = currentScene;
        this.sceneMap = sceneMap;
        this.alwaysRender = alwaysRender;
        this.blockInput = blockInput;
    }

    public void putSettings(String name, String value) {
        settings.put(name, value);
        sceneMap.values().forEach(scene -> scene.settingUpdated(name, value));
        sceneMap.values().forEach(scene -> scene.settingsUpdated(settings));
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
