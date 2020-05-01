package com.simple.castle.object.constructors;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import java.util.function.Function;

public enum RigidBodyTool {
    SPHERE(RigidBodyTool::calculateSphere),
    BASE_BOX(RigidBodyTool::calculateBaseBox),
    ADJUSTED_BOX(RigidBodyTool::calculateAdjustedBox);

    private static final float SCALAR = 0.5f;
    private final Function<BoundingBox, btCollisionShape> function;

    RigidBodyTool(Function<BoundingBox, btCollisionShape> function) {
        this.function = function;
    }

    private static btBoxShape calculateBaseBox(BoundingBox boundingBox) {
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btBoxShape(dimensions.scl(SCALAR));
    }

    private static btBoxShape calculateAdjustedBox(BoundingBox boundingBox) {
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btBoxShape(dimensions.scl(SCALAR));
    }

    private static btSphereShape calculateSphere(BoundingBox boundingBox) {
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btSphereShape(dimensions.scl(SCALAR).x);
    }

    public btCollisionShape calculate(BoundingBox boundingBox) {
        return function.apply(boundingBox);
    }
}
