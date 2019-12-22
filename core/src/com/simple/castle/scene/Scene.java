package com.simple.castle.scene;

import com.badlogic.gdx.InputProcessor;
import com.simple.castle.drawable.ApplicationDrawable;
import com.simple.castle.manager.ManagerContext;

import java.util.Map;

public abstract class Scene extends ApplicationDrawable {

    private InputProcessor inputProcessor;
    private ManagerContext managerContext;

    @Override
    public boolean keyDown(int keycode) {
        return inputProcessor != null && inputProcessor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputProcessor != null && inputProcessor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputProcessor != null && inputProcessor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputProcessor != null && inputProcessor.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputProcessor != null && inputProcessor.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputProcessor != null && inputProcessor.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return inputProcessor != null && inputProcessor.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return inputProcessor != null && inputProcessor.scrolled(amount);
    }

    public void settingUpdated(String name, String value) {

    }

    public void settingsUpdated(Map<String, String> settings) {

    }

    protected void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public ManagerContext getManagerContext() {
        if (managerContext == null) {
            throw new IllegalStateException("There is no context");
        }
        return managerContext;
    }

    public void setManagerContext(ManagerContext managerContext) {
        this.managerContext = managerContext;
    }

}
