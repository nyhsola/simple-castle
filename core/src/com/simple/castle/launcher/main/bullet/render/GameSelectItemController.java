package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.simple.castle.launcher.main.bullet.object.GameObject;

import java.util.List;

public class GameSelectItemController extends InputAdapter {

    private GameObject intersect(GameCamera gameCamera, List<GameObject> gameObjectList, int touchedX, int touchedY) {
        Ray pickRay = gameCamera.getPerspectiveCamera().getPickRay(touchedX, touchedY);
        Vector3 intersection = new Vector3();

        return gameObjectList.stream()
                .filter(gameObject -> Intersector.intersectRayBounds(pickRay, gameObject.calculateBoundingBox(new BoundingBox()), intersection))
                .findFirst()
                .orElse(null);
    }
}
