package com.simple.castle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.simple.castle.constants.Scenes;
import com.simple.castle.manager.Manager;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.game.GameScene;
import com.simple.castle.scenes.main.menu.MenuBackgroundScene;
import com.simple.castle.scenes.main.menu.MenuScene;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

public class MainScene extends Scene {

    private Manager mainSceneManager;

    @Override
    public void create() {
        FileHandle skinFileHandle = Gdx.files.internal(DEFAULT_UI_SKIN);

        mainSceneManager = new Manager.ManagerBuilder()
                .addScene(Scenes.MENU_SCENE, new MenuScene(skinFileHandle))
                .addScene(Scenes.MENU_BACKGROUND_SCENE, new MenuBackgroundScene(skinFileHandle))
                .addScene(Scenes.GAME_SCENE, new GameScene())
                .addAlwaysRender(Scenes.GAME_SCENE)
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
