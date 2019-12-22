package com.simple.castle.scenes;

import com.simple.castle.constants.Scenes;
import com.simple.castle.manager.Manager;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.game.GameScene;
import com.simple.castle.scenes.main.menu.MenuScene;

public class MainScene extends Scene {

    private Manager mainSceneManager;

    @Override
    public void create() {
        mainSceneManager = new Manager.ManagerBuilder()
                .addScene(Scenes.MENU_SCENE, new MenuScene())
                .addScene(Scenes.GAME_SCENE, new GameScene())
                .addAlwaysRender(Scenes.GAME_SCENE)
                .currentScene(Scenes.MENU_SCENE)
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
