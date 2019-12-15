package com.simple.castle.manager.impl;

import com.simple.castle.camera.CameraSettings;
import com.simple.castle.manager.BlockScene;
import com.simple.castle.manager.ChangeScene;
import com.simple.castle.manager.GetCameraSettings;
import com.simple.castle.manager.ManagerController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class DefaultManager extends Scene implements ManagerController, ChangeScene, BlockScene, GetCameraSettings {

    private String currentScene;
    private String startScene;
    private Map<String, Scene> sceneMap;
    private List<String> alwaysRender = new ArrayList<>();
    private List<String> blockInput = new ArrayList<>();
    private CameraSettings cameraSettings = new CameraSettings();

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

    public void update() {
        for (Scene scene : sceneMap.values()) {
            scene.update();
        }
        forEachAlwaysRender(Scene::update);
    }

    @Override
    public void render() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).render();
        }
        forEachAlwaysRender(Scene::render);
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
        forEachAlwaysRender(scene -> scene.resize(width, height));
    }

    @Override
    public void pause() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).pause();
        }
        forEachAlwaysRender(Scene::pause);
    }

    @Override
    public void resume() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).resume();
        }
        forEachAlwaysRender(Scene::resume);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyDown(keycode);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.keyDown(keycode));
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyUp(keycode);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.keyUp(keycode));
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyTyped(character);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.keyTyped(character));
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchDown(screenX, screenY, pointer, button);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.touchDown(screenX, screenY, pointer, button));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchUp(screenX, screenY, pointer, button);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.touchUp(screenX, screenY, pointer, button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchDragged(screenX, screenY, pointer);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.touchDragged(screenX, screenY, pointer));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).mouseMoved(screenX, screenY);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.mouseMoved(screenX, screenY));
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).scrolled(amount);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.scrolled(amount));
        return super.scrolled(amount);
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
    public CameraSettings getSettings() {
        return cameraSettings;
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
    public GetCameraSettings getCameraSettings() {
        return this;
    }

    private void forEachAlwaysRenderBlockInput(Consumer<Scene> screenConsumer) {
        for (String scene : alwaysRender) {
                screenConsumer.accept(sceneMap.get(scene));
        }
    }

    private void forEachAlwaysRender(Consumer<Scene> screenConsumer) {
        for (String scene : alwaysRender) {
            if (sceneMap.containsKey(scene)) {
                screenConsumer.accept(sceneMap.get(scene));
            }
        }
    }

}
