package com.simple.castle.scenes;

import java.util.Map;

import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.game.FullGameScene;
import com.simple.castle.scenes.main.menu.MenuScene;

public class MainScene extends Scene {

    @Override
    public void create() {
        manager
                .addScene(Scenes.MENU_SCENE, new MenuScene(this))
                .addScene(Scenes.FULL_GAME_SCENE, new FullGameScene(this))
                .addAlwaysRender(Scenes.FULL_GAME_SCENE)
                .setCurrentScene(Scenes.MENU_SCENE);
        super.create();
    }

    @Override
    public void fromChild(Map<String, Object> map) {
        if(map.containsKey(MenuScene.TO_BLOCK)){
            manager.blockInput((String) map.get(MenuScene.TO_BLOCK));
        }
        if(map.containsKey(MenuScene.TO_UNBLOCK)){
            manager.removeBlockInput((String) map.get(MenuScene.TO_UNBLOCK));
        }
        super.toChild(map);
    }
}
