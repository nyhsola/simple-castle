package com.simple.castle.manager.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;

public class Manager implements ApplicationListener, InputProcessor {

    private final ManagerContext managerContext = new ManagerContext();

    public Manager(String startScene, Map<String, Scene> sceneMap) {
        managerContext.setCurrentScene(startScene);
        managerContext.setSceneMap(Collections.unmodifiableMap(new HashMap<>(sceneMap)));
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender) {
        this(startScene, sceneMap);
        managerContext.setAlwaysRender(alwaysRender);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender,
                   List<String> blockInput) {
        this(startScene, sceneMap, alwaysRender);
        managerContext.setBlockInput(blockInput);
    }

    @Override
    public void create() {
        forEachScene(scene -> {
            scene.create();
            scene.setManagerContext(managerContext);
        });
    }

    public void update() {
        forEachScene(Scene::update);
        forEachAlwaysRender(Scene::update);
    }

    @Override
    public void render() {
        forCurrentScene(Scene::render);
        forEachAlwaysRender(Scene::render);
    }

    @Override
    public void dispose() {
        forEachScene(Scene::dispose);
    }

    @Override
    public void resize(int width, int height) {
        forCurrentScene(scene -> scene.resize(width, height));
        forEachAlwaysRender(scene -> scene.resize(width, height));
    }

    @Override
    public void pause() {
        forCurrentScene(Scene::pause);
        forEachAlwaysRender(Scene::pause);
    }

    @Override
    public void resume() {
        forCurrentScene(Scene::resume);
        forEachAlwaysRender(Scene::resume);
    }

    @Override
    public boolean keyDown(int keycode) {
        forCurrentScene(scene -> scene.keyDown(keycode));
        forEachAlwaysRenderBlockInput(scene -> scene.keyDown(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        forCurrentScene(scene -> scene.keyUp(keycode));
        forEachAlwaysRenderBlockInput(scene -> scene.keyUp(keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        forCurrentScene(scene -> scene.keyTyped(character));
        forEachAlwaysRenderBlockInput(scene -> scene.keyTyped(character));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        forCurrentScene(scene -> scene.touchDown(screenX, screenY, pointer, button));
        forEachAlwaysRenderBlockInput(scene -> scene.touchDown(screenX, screenY, pointer, button));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        forCurrentScene(scene -> scene.touchUp(screenX, screenY, pointer, button));
        forEachAlwaysRenderBlockInput(scene -> scene.touchUp(screenX, screenY, pointer, button));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        forCurrentScene(scene -> scene.touchDragged(screenX, screenY, pointer));
        forEachAlwaysRenderBlockInput(scene -> scene.touchDragged(screenX, screenY, pointer));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        forCurrentScene(scene -> scene.mouseMoved(screenX, screenY));
        forEachAlwaysRenderBlockInput(scene -> scene.mouseMoved(screenX, screenY));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        forCurrentScene(scene -> scene.scrolled(amount));
        forEachAlwaysRenderBlockInput(scene -> scene.scrolled(amount));
        return false;
    }

    private void forEachAlwaysRenderBlockInput(Consumer<Scene> screenConsumer) {
        for (String scene : managerContext.getAlwaysRender()) {
            screenConsumer.accept(managerContext.getSceneMap().get(scene));
        }
    }

    private void forEachAlwaysRender(Consumer<Scene> screenConsumer) {
        for (String scene : managerContext.getAlwaysRender()) {
            if (managerContext.getSceneMap().containsKey(scene)) {
                screenConsumer.accept(managerContext.getSceneMap().get(scene));
            }
        }
    }

    private void forCurrentScene(Consumer<Scene> screenConsumer) {
        if (managerContext.getSceneMap().containsKey(managerContext.getCurrentScene())) {
            screenConsumer.accept(managerContext.getSceneMap().get(managerContext.getCurrentScene()));
        }
    }

    private void forEachScene(Consumer<Scene> screenConsumer) {
        for (Scene scene : managerContext.getSceneMap().values()) {
            screenConsumer.accept(scene);
        }
    }

}
