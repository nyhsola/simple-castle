package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;

import java.util.stream.StreamSupport;

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
        gameObject = intersect(gameCamera, gamePhysicWorld.getInstances(), screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private GameObject intersect(GameCamera gameCamera, Iterable<GameObject> gameObjectList, int touchedX, int touchedY) {
        Ray pickRay = gameCamera.getPerspectiveCamera().getPickRay(touchedX, touchedY);
        Vector3 intersection = new Vector3();
        return StreamSupport.stream(gameObjectList.spliterator(), false)
                .filter(gameObject -> {
                    BoundingBox boundingBox = gameObject.calculateBoundingBox(tmp);
                    boundingBox.mul(gameObject.transform);
                    return Intersector.intersectRayBounds(pickRay, boundingBox, intersection);
                })
                .findAny()
                .orElse(null);
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
