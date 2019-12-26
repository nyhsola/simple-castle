package com.simple.castle.game.scenes.main.game;

import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.game.add.GameScene;

import java.util.Map;

public class FullGameScene extends Scene {

    public FullGameScene(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        manager
                .addScene(Scenes.GAME_SCENE, new GameScene(this))
                .setCurrentScene(Scenes.GAME_SCENE);
        super.create();
    }

    @Override
    public void fromParent(Map<String, Object> map) {
        super.toChild(map);
    }

    @Override
    public void fromChild(Map<String, Object> map) {
        super.toParent(map);
    }
}
