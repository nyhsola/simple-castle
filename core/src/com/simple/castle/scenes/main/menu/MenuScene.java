package com.simple.castle.scenes.main.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

public class MenuScene extends Scene {

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);
        manager
                .addScene(Scenes.MENU_SCENE_BACKGROUND, new MenuSceneBackground(skinFileHandle))
                .addScene(Scenes.MENU_SCENE_MENU, new MenuSceneMain(skinFileHandle))
                .setCurrentScene(Scenes.MENU_SCENE_BACKGROUND);
        super.create();
    }

//    @Override
//    public void settingUpdated(String name, String value) {
//        this.getManagerContext().putSettings(name, value);
//    }

}
