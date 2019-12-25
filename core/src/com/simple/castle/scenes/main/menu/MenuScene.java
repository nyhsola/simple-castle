package com.simple.castle.scenes.main.menu;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.menu.add.MenuSceneBackground;
import com.simple.castle.scenes.main.menu.add.MenuSceneMain;

public class MenuScene extends Scene {

    public static final String TO_SCENE = "State";
    public static final String TO_BLOCK = "Block";
    public static final String TO_UNBLOCK = "Unblock";
    public static final String CAMERA_FIELD_OF_VIEW = "CameraFieldOfView";

    public MenuScene(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);
        manager
                .addScene(Scenes.MENU_SCENE_BACKGROUND, new MenuSceneBackground(this, skinFileHandle))
                .addScene(Scenes.MENU_SCENE_MENU, new MenuSceneMain(this, skinFileHandle))
                .setCurrentScene(Scenes.MENU_SCENE_BACKGROUND);
        super.create();
    }

    @Override
    public void fromChild(Map<String, Object> map) {
        if (map.containsKey(TO_SCENE)) {
            manager.setCurrentScene((String) map.get(TO_SCENE));
        }
        super.toParent(map);
    }

    @Override
    public void fromParent(Map<String, Object> map) {
        super.toChild(map);
    }
}
