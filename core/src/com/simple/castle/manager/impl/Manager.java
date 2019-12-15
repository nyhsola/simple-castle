package com.simple.castle.manager.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Manager extends DefaultManager {

    public Manager(String startScene, Map<String, Scene> sceneMap) {
        super(startScene, sceneMap);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender) {
        super(startScene, sceneMap, alwaysRender);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender, List<String> blockInput) {
        super(startScene, sceneMap, alwaysRender, blockInput);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void render() {
        super.render();
        forEachAlwaysRender(Scene::render);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        forEachAlwaysRender(scene -> scene.resize(width, height));
    }

    @Override
    public void pause() {
        super.pause();
        forEachAlwaysRender(Scene::pause);
    }

    @Override
    public void resume() {
        super.resume();
        forEachAlwaysRender(Scene::resume);
    }

    @Override
    public boolean keyDown(int keycode) {
        super.keyDown(keycode);
        forEachAlwaysRenderBlockInput(scene -> scene.keyDown(keycode));
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        super.keyUp(keycode);
        forEachAlwaysRenderBlockInput(scene -> scene.keyUp(keycode));
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        super.keyTyped(character);
        forEachAlwaysRenderBlockInput(scene -> scene.keyTyped(character));
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        forEachAlwaysRenderBlockInput(scene -> scene.touchDown(screenX, screenY, pointer, button));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        forEachAlwaysRenderBlockInput(scene -> scene.touchUp(screenX, screenY, pointer, button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        super.touchDragged(screenX, screenY, pointer);
        forEachAlwaysRenderBlockInput(scene -> scene.touchDragged(screenX, screenY, pointer));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        super.mouseMoved(screenX, screenY);
        forEachAlwaysRenderBlockInput(scene -> scene.mouseMoved(screenX, screenY));
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        super.scrolled(amount);
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
