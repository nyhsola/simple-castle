package com.simple.castle.utils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

public class GameObjectsUtil {

    private static final float SCALAR = 0.5f;

    private GameObjectsUtil() {
    }

    public static btBoxShape calculateBox(BoundingBox boundingBox) {
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btBoxShape(dimensions.scl(SCALAR));
    }

    public static btSphereShape calculateSphere(BoundingBox boundingBox) {
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btSphereShape(dimensions.scl(SCALAR).x);
    }
}
