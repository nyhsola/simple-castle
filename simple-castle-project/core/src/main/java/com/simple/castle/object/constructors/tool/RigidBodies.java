package com.simple.castle.object.constructors.tool;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import java.util.function.Function;

public enum RigidBodies {
    SPHERE(RigidBodies::calculateSphere),
    BASE_BOX(RigidBodies::calculateBaseBox),
    ADJUSTED_BOX(RigidBodies::calculateAdjustedBox);

    private static final float SCALAR = 0.5f;

    private final Function<Node, btCollisionShape> function;

    RigidBodies(Function<Node, btCollisionShape> function) {
        this.function = function;
    }

    private static btBoxShape calculateBaseBox(Node node) {
        final BoundingBox temp = new BoundingBox();
        BoundingBox boundingBox = node.calculateBoundingBox(temp);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        float max = Math.max(dimensions.x, dimensions.z);
        return new btBoxShape(new Vector3(max, dimensions.y, max).scl(SCALAR));
    }

    private static btBoxShape calculateAdjustedBox(Node node) {
        final BoundingBox temp = new BoundingBox();
        BoundingBox boundingBox = node.calculateBoundingBox(temp);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btBoxShape(dimensions.scl(SCALAR));
    }

    private static btSphereShape calculateSphere(Node node) {
        final BoundingBox temp = new BoundingBox();
        BoundingBox boundingBox = node.calculateBoundingBox(temp);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btSphereShape(dimensions.scl(SCALAR).x);
    }

    public btCollisionShape calculate(Node node) {
        return function.apply(node);
    }
}
