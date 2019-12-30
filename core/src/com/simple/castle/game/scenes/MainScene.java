package com.simple.castle.game.scenes;

import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.game.FullGameScene;
import com.simple.castle.game.scenes.main.menu.MenuScene;

import java.util.Map;

public class MainScene extends Scene {

    public MainScene(Scene parent) {
        super(parent);
    }

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
    public void onChildEvent(Map<String, Object> map) {
        if (map.containsKey(MenuScene.TO_BLOCK)) {
            manager.blockInput((String) map.get(MenuScene.TO_BLOCK));
        }
        if (map.containsKey(MenuScene.TO_UNBLOCK)) {
            manager.removeBlockInput((String) map.get(MenuScene.TO_UNBLOCK));
        }
        super.notifyAllChildren(map);
    }
}
