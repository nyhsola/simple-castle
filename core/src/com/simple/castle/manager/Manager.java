package com.simple.castle.manager;

import com.simple.castle.scenes.ChangeScene;
import com.simple.castle.scenes.abs.Scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Manager extends Scene implements ChangeScene {

    private String startScene;
    private String currentScene;

    private Map<String, Scene> sceneMap;
    private List<String> alwaysRender = Collections.emptyList();

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender) {
        this(startScene, sceneMap);
        this.alwaysRender = Collections.unmodifiableList(new ArrayList<>(alwaysRender));
    }

    public Manager(String startScene, Map<String, Scene> sceneMap) {
        this.sceneMap = Collections.unmodifiableMap(new LinkedHashMap<>(sceneMap));
        this.startScene = startScene;
    }

    @Override
    public void create() {
        for (Scene scene : sceneMap.values()) {
            scene.setChangeScene(this);
        }

        this.setScene(startScene);

        for (Scene scene : sceneMap.values()) {
            scene.create();
        }
    }

    @Override
    public void render() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).render();
        }

        forEachAlwaysScene(Scene::render);
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

        forEachAlwaysScene(scene -> scene.resize(width, height));
    }

    @Override
    public void pause() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).pause();
        }
        forEachAlwaysScene(Scene::pause);
    }

    @Override
    public void resume() {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).resume();
        }
        forEachAlwaysScene(Scene::resume);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyDown(keycode);
        }
        forEachAlwaysScene(scene -> scene.keyDown(keycode));
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyUp(keycode);
        }
        forEachAlwaysScene(scene -> scene.keyUp(keycode));
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).keyTyped(character);
        }
        forEachAlwaysScene(scene -> scene.keyTyped(character));
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchDown(screenX, screenY, pointer, button);
        }
        forEachAlwaysScene(scene -> scene.touchDown(screenX, screenY, pointer, button));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchUp(screenX, screenY, pointer, button);
        }
        forEachAlwaysScene(scene -> scene.touchUp(screenX, screenY, pointer, button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).touchDragged(screenX, screenY, pointer);
        }
        forEachAlwaysScene(scene -> scene.touchDragged(screenX, screenY, pointer));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).mouseMoved(screenX, screenY);
        }
        forEachAlwaysScene(scene -> scene.mouseMoved(screenX, screenY));
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        if (sceneMap.containsKey(currentScene)) {
            sceneMap.get(currentScene).scrolled(amount);
        }
        forEachAlwaysScene(scene -> scene.scrolled(amount));
        return super.scrolled(amount);
    }

    @Override
    public void setChangeScene(ChangeScene changeScene) {
        super.setChangeScene(changeScene);
    }

    @Override
    public void setScene(String scene) {
        if (sceneMap.containsKey(scene)) {
            currentScene = scene;
        }
    }

    private void forEachAlwaysScene(Consumer<Scene> screenConsumer) {
        for (String alwaysRenderScene : alwaysRender) {
            if (sceneMap.containsKey(alwaysRenderScene)) {
                screenConsumer.accept(sceneMap.get(alwaysRenderScene));
            }
        }
    }

}
