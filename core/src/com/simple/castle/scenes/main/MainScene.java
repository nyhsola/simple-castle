package com.simple.castle.scenes.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.manager.impl.Manager;
import com.simple.castle.manager.impl.Scene;
import com.simple.castle.scenes.main.game.GameScene;
import com.simple.castle.scenes.main.menu.MenuBackgroundScene;
import com.simple.castle.scenes.main.menu.MenuScene;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

public class MainScene extends Scene {

    public static final String GAME_SCENE = "GAME_SCENE";
    public static final String MENU_SCENE = "MENU_SCENE";
    public static final String MENU_BACKGROUND_SCENE = "MENU_BACKGROUND_SCENE";

    private Manager mainSceneManager;

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);

        mainSceneManager = new Manager.ManagerBuilder()
                .addScene(MENU_SCENE, new MenuScene(skinFileHandle))
                .addScene(MENU_BACKGROUND_SCENE, new MenuBackgroundScene(skinFileHandle))
                .addScene(GAME_SCENE, new GameScene())
                .addAlwaysRender(GAME_SCENE)
                .build();
        mainSceneManager.create();

        this.setInputProcessor(mainSceneManager);
    }

    @Override
    public void update() {
        mainSceneManager.update();
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
