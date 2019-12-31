package com.simple.castle.drawable.scene;

import com.badlogic.gdx.InputProcessor;
import com.simple.castle.drawable.application.ApplicationParent;
import com.simple.castle.drawable.manager.Manager;

public abstract class SceneManaged extends ApplicationParent implements InputProcessor {

    private InputProcessor inputProcessor;
    protected final Manager manager = new Manager();

    public SceneManaged() {
    }

    public SceneManaged(Scene parent) {
        super(parent);
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
    public void update(float delta) {
        manager.update(delta);
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

    public void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

}
