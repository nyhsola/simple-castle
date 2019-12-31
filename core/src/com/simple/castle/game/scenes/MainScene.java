package com.simple.castle.game.scenes;

import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.game.GameScene;
import com.simple.castle.game.scenes.main.menu.MenuScene;

public class MainScene extends Scene {

    @Override
    public void create() {
        manager
                .addScene(Scenes.MENU_SCENE, new MenuScene())
                .addScene(Scenes.GAME_SCENE, new GameScene())
                .addAlwaysRender(Scenes.GAME_SCENE)
                .setCurrentScene(Scenes.MENU_SCENE);
        super.create();
    }

}
