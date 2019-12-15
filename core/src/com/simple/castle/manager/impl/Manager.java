package com.simple.castle.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Manager extends Scene {

    private String startScene;

    private final Map<String, Scene> sceneMap;

    private List<String> alwaysRender = Collections.emptyList();
    private List<String> blockInput = Collections.emptyList();

    private DefaultManagerController defaultManagerController;

    public Manager(String startScene, Map<String, Scene> sceneMap) {
        this.startScene = startScene;
        this.sceneMap = Collections.unmodifiableMap(new LinkedHashMap<>(sceneMap));

        this.defaultManagerController = new DefaultManagerController(startScene, sceneMap);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender) {
        this(startScene, sceneMap);
        this.alwaysRender = Collections.unmodifiableList(new ArrayList<>(alwaysRender));

        this.defaultManagerController = new DefaultManagerController(startScene, sceneMap);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender, List<String> blockInput) {
        this(startScene, sceneMap, alwaysRender);
        this.blockInput = Collections.unmodifiableList(new ArrayList<>(blockInput));

        this.defaultManagerController = new DefaultManagerController(startScene, sceneMap, blockInput);
    }

    @Override
    public void create() {
        for (Scene scene : sceneMap.values()) {
            scene.setManagerController(defaultManagerController);
        }

        defaultManagerController.changeScene(startScene);

        for (Scene scene : sceneMap.values()) {
            scene.create();
        }
    }

    @Override
    public void render() {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).render();
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
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).resize(width, height);
        }

        forEachAlwaysRender(scene -> scene.resize(width, height));
    }

    @Override
    public void pause() {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).pause();
        }
        forEachAlwaysRender(Scene::pause);
    }

    @Override
    public void resume() {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).resume();
        }
        forEachAlwaysRender(Scene::resume);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).keyDown(keycode);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.keyDown(keycode));
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).keyUp(keycode);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.keyUp(keycode));
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).keyTyped(character);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.keyTyped(character));
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).touchDown(screenX, screenY, pointer, button);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.touchDown(screenX, screenY, pointer, button));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).touchUp(screenX, screenY, pointer, button);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.touchUp(screenX, screenY, pointer, button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).touchDragged(screenX, screenY, pointer);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.touchDragged(screenX, screenY, pointer));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).mouseMoved(screenX, screenY);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.mouseMoved(screenX, screenY));
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        if (sceneMap.containsKey(defaultManagerController.getCurrentScene())) {
            sceneMap.get(defaultManagerController.getCurrentScene()).scrolled(amount);
        }
        forEachAlwaysRenderBlockInput(scene -> scene.scrolled(amount));
        return super.scrolled(amount);
    }

    private void forEachAlwaysRenderBlockInput(Consumer<Scene> screenConsumer) {
        for (String scene : alwaysRender) {
            if (sceneMap.containsKey(scene) && !blockInput.contains(scene)) {
                screenConsumer.accept(sceneMap.get(scene));
            }
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
