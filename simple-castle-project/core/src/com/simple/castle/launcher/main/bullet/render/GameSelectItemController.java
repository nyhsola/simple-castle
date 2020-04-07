package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;
import com.simple.castle.launcher.main.utils.GameIntersectUtils;

public class GameSelectItemController extends InputAdapter {

    private final GameCamera gameCamera;
    private final GamePhysicWorld gamePhysicWorld;

    private GameObject gameObject;

    private BoundingBox tmp = new BoundingBox();
    private Vector3 tmpVector3 = new Vector3();

    public GameSelectItemController(GameCamera gameCamera, GamePhysicWorld gamePhysicWorld) {
        this.gameCamera = gameCamera;
        this.gamePhysicWorld = gamePhysicWorld;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        gameObject = GameIntersectUtils.intersect(tmp, gameCamera, gamePhysicWorld.getInstances(), screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
