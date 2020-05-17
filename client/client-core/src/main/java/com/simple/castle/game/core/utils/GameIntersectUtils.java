package com.simple.castle.game.core.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.server.game.core.render.BaseCamera;

import java.util.stream.StreamSupport;

public final class GameIntersectUtils {

    private GameIntersectUtils() {

    }

    public static AbstractGameObject intersect(BoundingBox tmp, BaseCamera baseCamera, Iterable<AbstractGameObject> gameObjectList, int touchedX, int touchedY) {
        Ray pickRay = baseCamera.getPickRay(touchedX, touchedY);
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

    public static Vector3 intersectPositionPoint(BoundingBox tmp, BaseCamera baseCamera, AbstractGameObject abstractGameObject, int touchedX, int touchedY) {
        Ray pickRay = baseCamera.getPickRay(touchedX, touchedY);
        Vector3 intersection = new Vector3();

        BoundingBox boundingBox = abstractGameObject.calculateBoundingBox(tmp);
        boundingBox.mul(abstractGameObject.transform);

        if (Intersector.intersectRayBounds(pickRay, boundingBox, intersection)) {
            return intersection;
        }

        return null;
    }
}
