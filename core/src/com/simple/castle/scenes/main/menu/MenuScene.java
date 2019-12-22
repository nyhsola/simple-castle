package com.simple.castle.scenes.main.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.constants.Scenes;
import com.simple.castle.manager.Manager;
import com.simple.castle.scene.Scene;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

public class MenuScene extends Scene {

    private Manager menuSceneManger;

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);
        menuSceneManger = new Manager.ManagerBuilder()
                .addScene(Scenes.MENU_SCENE_BACKGROUND, new MenuSceneBackground(skinFileHandle))
                .addScene(Scenes.MENU_SCENE_MENU, new MenuSceneMain(skinFileHandle))
                .currentScene(Scenes.MENU_SCENE_BACKGROUND)
                .build();
        menuSceneManger.create();
        this.setInputProcessor(menuSceneManger);
    }

    @Override
    public void settingUpdated(String name, String value) {
        this.getManagerContext().putSettings(name, value);
    }

    @Override
    public void update() {
        menuSceneManger.update();
    }

    @Override
    public void render() {
        menuSceneManger.render();
    }

    @Override
    public void dispose() {
        menuSceneManger.dispose();
    }

}
