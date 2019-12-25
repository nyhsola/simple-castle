package com.simple.castle.scenes.main.game;

import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.game.add.BackgroundParamsScene;
import com.simple.castle.scenes.main.game.add.GameScene;

import java.util.Map;

public class FullGameScene extends Scene {

    public FullGameScene(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        manager
                .addScene(Scenes.GAME_SCENE, new GameScene(this))
                .addScene(Scenes.BACKGROUND_PARAMS, new BackgroundParamsScene(this))
                .setCurrentScene(Scenes.GAME_SCENE)
                .addAlwaysRender(Scenes.BACKGROUND_PARAMS);
        super.create();
    }

    @Override
    public void fromParent(Map<String, Object> map) {
        super.toChild(map);
    }
}
