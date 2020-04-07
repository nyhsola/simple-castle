package com.simple.castle.launcher.main.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;

import java.util.stream.StreamSupport;

public class GameIntersectUtils {

    public static GameObject intersect(BoundingBox tmp, GameCamera gameCamera, Iterable<GameObject> gameObjectList, int touchedX, int touchedY) {
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

    public static Vector3 intersectPositionPoint(BoundingBox tmp, GameCamera gameCamera, GameObject gameObject, int touchedX, int touchedY) {
        Ray pickRay = gameCamera.getPerspectiveCamera().getPickRay(touchedX, touchedY);
        Vector3 intersection = new Vector3();

        BoundingBox boundingBox = gameObject.calculateBoundingBox(tmp);
        boundingBox.mul(gameObject.transform);

        if (Intersector.intersectRayBounds(pickRay, boundingBox, intersection)) {
            return intersection;
        }

        return null;
    }
}
