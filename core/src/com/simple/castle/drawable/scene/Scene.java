package com.simple.castle.drawable.scene;

import com.badlogic.gdx.InputProcessor;
import com.simple.castle.drawable.ApplicationDrawable;
import com.simple.castle.drawable.manager.Manager;

import java.util.Map;

public abstract class Scene extends ApplicationDrawable implements SceneEvent {

    private final Scene parent;
    private InputProcessor inputProcessor;
    protected Manager manager = new Manager();

    public Scene() {
        this.parent = null;
    }

    public Scene(Scene parent) {
        this.parent = parent;
    }

    @Override
    public void notifyParent(Map<String, Object> map) {
        if (parent != null) {
            parent.onChildEvent(map);
        }
    }

    @Override
    public void notifyAllChildren(Map<String, Object> map) {
        manager.notifyAllChildren(map);
    }

    @Override
    public void onParentEvent(Map<String, Object> map) {
        this.notifyAllChildren(map);
    }

    @Override
    public void onChildEvent(Map<String, Object> map) {
        this.notifyParent(map);
    }

    @Override
    public void create() {
        manager.create();
    }

    @Override
    public void resize(int width, int height) {
        manager.resize(width, height);
    }

    @Override
    public void update() {
        manager.update();
    }

    @Override
    public void render() {
        manager.render();
    }

    @Override
    public void pause() {
        manager.pause();
    }

    @Override
    public void resume() {
        manager.resume();
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputProcessor != null
                ? inputProcessor.keyDown(keycode)
                : manager.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputProcessor != null
                ? inputProcessor.keyUp(keycode)
                : manager.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputProcessor != null
                ? inputProcessor.keyTyped(character)
                : manager.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputProcessor != null
                ? inputProcessor.touchDown(screenX, screenY, pointer, button)
                : manager.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputProcessor != null
                ? inputProcessor.touchUp(screenX, screenY, pointer, button)
                : manager.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputProcessor != null
                ? inputProcessor.touchDragged(screenX, screenY, pointer)
                : manager.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return inputProcessor != null
                ? inputProcessor.mouseMoved(screenX, screenY)
                : manager.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return inputProcessor != null
                ? inputProcessor.scrolled(amount)
                : manager.scrolled(amount);
    }

    /**
     * If you set this, parent scenes will be ignored
     *
     * @param inputProcessor
     */
    public void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

}
