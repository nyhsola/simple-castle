package com.simple.castle.scenes;

import java.util.Map;

import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.game.GameScene;
import com.simple.castle.scenes.main.menu.MenuScene;

public class MainScene extends Scene {

    public MainScene(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        manager
                .addScene(Scenes.MENU_SCENE, new MenuScene(this))
                .addScene(Scenes.GAME_SCENE, new GameScene())
                .addAlwaysRender(Scenes.GAME_SCENE)
                .setCurrentScene(Scenes.MENU_SCENE);
        super.create();
    }

    @Override
    public void triggerParent(Map<String, Object> map) {
        if(map.containsKey(MenuScene.TO_BLOCK)){
            manager.blockInput((String) map.get(MenuScene.TO_BLOCK));
        }
        if(map.containsKey(MenuScene.TO_UNBLOCK)){
            manager.removeBlockInput((String) map.get(MenuScene.TO_UNBLOCK));
        }
        super.triggerChild(map);
    }
}
