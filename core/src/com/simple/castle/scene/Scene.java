package com.simple.castle.scene;

import com.simple.castle.drawable.ApplicationDrawable;
import com.simple.castle.manager.Manager;
import com.simple.castle.manager.ManagerContext;

public abstract class Scene extends ApplicationDrawable {

    protected Manager manager = new Manager.ManagerBuilder().build();

    public ManagerContext getManagerContext() {
        return manager.getManagerContext();
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
        return manager.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return manager.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return manager.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return manager.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return manager.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return manager.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return manager.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return manager.scrolled(amount);
    }

}
