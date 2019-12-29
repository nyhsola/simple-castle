package com.simple.castle.game.scenes.main.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.menu.add.MenuSceneBackground;
import com.simple.castle.game.scenes.main.menu.add.MenuSceneMain;

import java.util.Map;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

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
    public void onChildEvent(Map<String, Object> map) {
        if (map.containsKey(TO_SCENE)) {
            manager.setCurrentScene((String) map.get(TO_SCENE));
        }
        super.onChildEvent(map);
    }

}
