package com.simple.castle.server.composition.add;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

import java.util.function.Function;

public enum PhysicShape {
    STATIC(PhysicShape::calculateStaticNodeShape),
    BASE_BOX(PhysicShape::calculateBaseBox),
    ADJUSTED_BASE_BOX(PhysicShape::calculateAdjustedBox);

    private static final float SCALAR = 0.5f;
    private final Function<Node, btCollisionShape> function;

    PhysicShape(Function<Node, btCollisionShape> function) {
        this.function = function;
    }

    private static btCollisionShape calculateStaticNodeShape(Node node) {
        return Bullet.obtainStaticNodeShape(node, false);
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

    public btCollisionShape build(Node node) {
        return this.function.apply(node);
    }
}
