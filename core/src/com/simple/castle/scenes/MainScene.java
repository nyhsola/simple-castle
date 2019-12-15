package com.simple.castle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.ChangeScene;
import com.simple.castle.Manager;

import java.util.HashMap;
import java.util.Map;

import static com.simple.castle.Constants.DEFAULT_UI_SKIN;

public class MainScene extends Scene {

    public static final String MENU_SCENE = "MENU_SCENE";
    public static final String MENU_BACKGROUND_SCENE = "MENU_BACKGROUND_SCENE";

    private ChangeScene changeScene;

    private Manager manager;

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);

        Map<String, Scene> sceneMap = new HashMap<>();
        sceneMap.put(MENU_SCENE, new MenuScene(skinFileHandle));
        sceneMap.put(MENU_BACKGROUND_SCENE, new MenuBackgroundScene(skinFileHandle));

        manager = new Manager(MENU_BACKGROUND_SCENE, sceneMap);
        manager.create();
    }

    @Override
    public void render() {
        manager.render();
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

    @Override
    public void setChangeScene(ChangeScene changeScene) {
        this.changeScene = changeScene;
    }


}
