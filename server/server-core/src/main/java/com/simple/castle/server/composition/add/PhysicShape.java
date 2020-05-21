package com.simple.castle.server.composition.add;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

import java.util.function.Function;

public enum PhysicShape {
    STATIC(PhysicShape::calculateStaticNodeShape);

    private final Function<Node, btCollisionShape> function;

    PhysicShape(Function<Node, btCollisionShape> function) {
        this.function = function;
    }

    private static btCollisionShape calculateStaticNodeShape(Node node) {
        return Bullet.obtainStaticNodeShape(node, false);
    }

    public btCollisionShape build(Node node) {
        return this.function.apply(node);
    }
}
