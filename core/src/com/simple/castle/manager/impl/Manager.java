package com.simple.castle.manager.impl;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Manager implements ApplicationListener, InputProcessor {

    private final ManagerContext managerContext;

    private Manager(ManagerBuilder managerBuilder) {
        managerContext = new ManagerContext(managerBuilder.sceneMap, managerBuilder.alwaysRender, managerBuilder.blockInput);
    }

    @Override
    public void create() {
        forEachScene(scene -> {
            scene.setManagerContext(managerContext);
            scene.create();
        });
    }

    public void update() {
        forEachScene(Scene::update);
        forEachAlwaysRender(Scene::update);
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

    public static class ManagerBuilder {

        private Map<String, Scene> sceneMap = new HashMap<>();
        private List<String> alwaysRender = new ArrayList<>();
        private List<String> blockInput = new ArrayList<>();

        public ManagerBuilder addScene(String sceneName, Scene scene) {
            sceneMap.put(sceneName, scene);
            return this;
        }

        public ManagerBuilder addAlwaysRender(String sceneName) {
            alwaysRender.add(sceneName);
            return this;
        }

        public ManagerBuilder blockInput(String sceneName) {
            blockInput.add(sceneName);
            return this;
        }

        public Manager build() {
            return new Manager(this);
        }
    }

}
