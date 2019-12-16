package com.simple.castle.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerContext {

    private final Map<String, String> settings = new HashMap<>();

    private String currentScene = "NO_SCENE";
    private Map<String, Scene> sceneMap = new HashMap<>();
    private List<String> alwaysRender = new ArrayList<>();
    private List<String> blockInput = new ArrayList<>();

    public String getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
    }

    public Map<String, Scene> getSceneMap() {
        return sceneMap;
    }

    public void setSceneMap(Map<String, Scene> sceneMap) {
        this.sceneMap = sceneMap;
    }

    public List<String> getAlwaysRender() {
        return alwaysRender;
    }

    public void setAlwaysRender(List<String> alwaysRender) {
        this.alwaysRender = alwaysRender;
    }

    public List<String> getBlockInput() {
        return blockInput;
    }

    public void setBlockInput(List<String> blockInput) {
        this.blockInput = blockInput;
    }

    public Map<String, String> getSettings() {
        return settings;
    }
}
