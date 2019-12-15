package com.simple.castle.manager.impl;

import com.simple.castle.manager.BlockScene;
import com.simple.castle.manager.ChangeScene;
import com.simple.castle.manager.ManagerController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultManagerController implements ManagerController, ChangeScene, BlockScene {

    private final Map<String, Scene> sceneMap;

    private String currentScene;
    private List<String> blockInput = Collections.emptyList();

    public DefaultManagerController(String currentScene, Map<String, Scene> sceneMap) {
        this.currentScene = currentScene;
        this.sceneMap = sceneMap;
    }

    public DefaultManagerController(String currentScene, Map<String, Scene> sceneMap, List<String> blockInput) {
        this(currentScene, sceneMap);
        this.blockInput = blockInput;
    }

    @Override
    public ChangeScene getChangeScene() {
        return this;
    }

    @Override
    public BlockScene getBlockScene() {
        return this;
    }

    @Override
    public void changeScene(String scene) {
        if (sceneMap.containsKey(scene)) {
            currentScene = scene;
        }
    }

    @Override
    public void addBlockScene(String scene) {
        if (sceneMap.containsKey(scene)) {
            blockInput.add(scene);
        }
    }

    @Override
    public void deleteBlockScene(String scene) {
        if (sceneMap.containsKey(scene)) {
            blockInput.remove(scene);
        }
    }

    public String getCurrentScene() {
        return currentScene;
    }
}
