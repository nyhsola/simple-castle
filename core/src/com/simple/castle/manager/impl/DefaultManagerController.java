package com.simple.castle.manager.impl;

import com.simple.castle.manager.BlockScene;
import com.simple.castle.manager.ChangeScene;
import com.simple.castle.manager.ManagerController;

public class DefaultManagerController implements ManagerController, ChangeScene, BlockScene {

    private String currentScene;

    @Override
    public ChangeScene getChangeScene() {
        return this;
    }

    @Override
    public BlockScene getBlockScene() {
        return this;
    }

    @Override
    public void addBlockScene(String scene) {

    }

    @Override
    public void deleteBlockScene(String scene) {

    }

    @Override
    public void changeScene(String scene) {
        this.currentScene = scene;
    }

    public String getCurrentScene() {
        return currentScene;
    }
}
