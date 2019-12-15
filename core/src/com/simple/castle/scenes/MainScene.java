package com.simple.castle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.manager.Manager;
import com.simple.castle.scenes.abs.Scene;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.simple.castle.Constants.DEFAULT_UI_SKIN;

public class MainScene extends Scene {

    public static final String GAME_SCENE = "GAME_SCENE";
    public static final String MENU_SCENE = "MENU_SCENE";
    public static final String MENU_BACKGROUND_SCENE = "MENU_BACKGROUND_SCENE";

    private Manager mainSceneManager;

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);

        Map<String, Scene> sceneMap = new HashMap<>();
        sceneMap.put(MENU_SCENE, new MenuScene(skinFileHandle));
        sceneMap.put(MENU_BACKGROUND_SCENE, new MenuBackgroundScene(skinFileHandle));
        sceneMap.put(GAME_SCENE, new GameScene());

        mainSceneManager = new Manager(MENU_BACKGROUND_SCENE, sceneMap, Collections.singletonList(GAME_SCENE));
        mainSceneManager.create();

        this.setInputProcessor(mainSceneManager);
    }

    @Override
    public void render() {
        mainSceneManager.render();
    }

    @Override
    public void dispose() {
        mainSceneManager.dispose();
    }

}
