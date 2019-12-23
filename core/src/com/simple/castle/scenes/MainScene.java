package com.simple.castle.scenes;

import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.game.GameScene;
import com.simple.castle.scenes.main.menu.MenuScene;

public class MainScene extends Scene {

    @Override
    public void create() {
        manager.addScene(Scenes.MENU_SCENE, new MenuScene())
                .addScene(Scenes.GAME_SCENE, new GameScene())
                .addAlwaysRender(Scenes.GAME_SCENE)
                .currentScene(Scenes.MENU_SCENE);
        super.create();
    }

}
