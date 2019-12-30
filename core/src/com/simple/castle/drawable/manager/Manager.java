package com.simple.castle.drawable.manager;

import com.simple.castle.drawable.ApplicationDrawable;
import com.simple.castle.drawable.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Manager extends ApplicationDrawable {

    private final Map<String, Scene> sceneMap = new HashMap<>();
    private final List<String> alwaysRender = new ArrayList<>();
    private final List<String> blockInput = new ArrayList<>();
    private String currentScene;

    public Manager addScene(String sceneName, Scene scene) {
        sceneMap.put(sceneName, scene);
        return this;
    }

    public Manager addAlwaysRender(String sceneName) {
        alwaysRender.add(sceneName);
        return this;
    }

    public Manager setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
        return this;
    }

    public Manager blockInput(String sceneName) {
        blockInput.add(sceneName);
        return this;
    }

    public Manager removeBlockInput(String sceneName) {
        blockInput.remove(sceneName);
        return this;
    }

    public void notifyAllChildren(Map<String, Object> map) {
        for (Scene scene : sceneMap.values()) {
            scene.onParentEvent(map);
        }
    }

    @Override
    public void create() {
        forEachScene(ApplicationDrawable::create);
    }

    @Override
    public void update(float delta) {
        forEachScene(scene -> scene.update(delta));
    }

    @Override
    public void render() {
        forEachAlwaysRender(Scene::render);
        forCurrentScene(Scene::render);
    }

    @Override
    public void dispose() {
        forEachScene(Scene::dispose);
    }

    @Override
    public void resize(int width, int height) {
        forEachScene(scene -> scene.resize(width, height));
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
        for (String scene : alwaysRender) {
            if(!blockInput.contains(scene)) {
                screenConsumer.accept(sceneMap.get(scene));
            }
        }
    }

    //TODO if current already, do not draw\or else
    private void forEachAlwaysRender(Consumer<Scene> screenConsumer) {
        for (String scene : alwaysRender) {
            if (sceneMap.containsKey(scene)) {
                screenConsumer.accept(sceneMap.get(scene));
            }
        }
    }

    private void forCurrentScene(Consumer<Scene> screenConsumer) {
        if (sceneMap.containsKey(currentScene)) {
            screenConsumer.accept(sceneMap.get(currentScene));
        }
    }

    private void forEachScene(Consumer<Scene> screenConsumer) {
        for (Scene scene : sceneMap.values()) {
            screenConsumer.accept(scene);
        }
    }

}
