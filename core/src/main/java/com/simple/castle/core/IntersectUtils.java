package com.simple.castle.core;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.simple.castle.core.render.BaseCamera;

public class IntersectUtils {
    public static Vector3 intersectPositionPoint(BoundingBox tmp, BaseCamera baseCamera, ModelInstance modelInstance, int touchedX, int touchedY) {
        Ray pickRay = baseCamera.getPickRay(touchedX, touchedY);
        Vector3 intersection = new Vector3();

        BoundingBox boundingBox = modelInstance.calculateBoundingBox(tmp);
        boundingBox.mul(modelInstance.transform);

        if (Intersector.intersectRayBounds(pickRay, boundingBox, intersection)) {
            return intersection;
        }

        return null;
    }
}
