package com.simple.castle.manager.impl;

import com.badlogic.gdx.InputProcessor;
import com.simple.castle.manager.BlockScene;
import com.simple.castle.manager.ChangeScene;
import com.simple.castle.manager.ManagerController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DefaultManager extends Scene implements ManagerController, ChangeScene, BlockScene {

    protected String startScene;
    protected Map<String, Scene> sceneMap;
    protected List<String> alwaysRender = new ArrayList<>();
    protected List<String> blockInput = new ArrayList<>();
    private String currentScene;

    public DefaultManager(String startScene, Map<String, Scene> sceneMap) {
        this.startScene = startScene;
        this.sceneMap = Collections.unmodifiableMap(new HashMap<>(sceneMap));
    }

    public DefaultManager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender) {
        this(startScene, sceneMap);
        this.alwaysRender = alwaysRender;
    }

    public DefaultManager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender, List<String> blockInput) {
        this(startScene, sceneMap, alwaysRender);
        this.blockInput = blockInput;
    }

    @Override
    public void create() {
        for (Scene scene : sceneMap.values()) {
            scene.create();
        }

        for (Scene scene : sceneMap.values()) {
            scene.setManagerController(this);
        }

        this.changeScene(startScene);
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
    public void resize(int width, int height) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).resize(width, height);
        }
    }

    @Override
    public void pause() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).pause();
        }
    }

    @Override
    public void resume() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).resume();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyTyped(character);
        }
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchDown(screenX, screenY, pointer, button);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchUp(screenX, screenY, pointer, button);
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchDragged(screenX, screenY, pointer);
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).mouseMoved(screenX, screenY);
        }
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).scrolled(amount);
        }
        return super.scrolled(amount);
    }

    @Override
    public void setManagerController(ManagerController managerController) {
        super.setManagerController(managerController);
    }

    @Override
    protected void setInputProcessor(InputProcessor inputProcessor) {
        super.setInputProcessor(inputProcessor);
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

    @Override
    public void changeScene(String scene) {
        currentScene = scene;
    }

    @Override
    public ChangeScene getChangeScene() {
        return this;
    }

    @Override
    public BlockScene getBlockScene() {
        return this;
    }
}
