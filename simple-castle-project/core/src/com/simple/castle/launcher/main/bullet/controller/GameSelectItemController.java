package com.simple.castle.launcher.main.bullet.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.scene.game.GameScene;
import com.simple.castle.launcher.main.utils.GameIntersectUtils;

public class GameSelectItemController extends InputAdapter {

    private final GameCamera gameCamera;
    private final BoundingBox tempBoundingBox = new BoundingBox();
    private final GameScene gameScene;

    private AbstractGameObject abstractGameObject;

    public GameSelectItemController(GameCamera gameCamera, GameScene gameScene) {
        this.gameCamera = gameCamera;
        this.gameScene = gameScene;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        abstractGameObject = GameIntersectUtils.intersect(tempBoundingBox, gameCamera, gameScene.getSceneGameObjects().values(), screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public AbstractGameObject getSelectedObject() {
        return abstractGameObject;
    }
}
