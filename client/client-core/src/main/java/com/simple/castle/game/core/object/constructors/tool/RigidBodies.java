package com.simple.castle.game.core.object.constructors.tool;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;

import java.util.function.Function;

public enum RigidBodies {
    SPHERE(RigidBodies::calculateSphere),
    BASE_BOX(RigidBodies::calculateBaseBox),
    ADJUSTED_BOX(RigidBodies::calculateAdjustedBox),
    CONVEX_HULL_OPTIMIZED(node -> createConvexHullShape(node, true)),
    CONVEX_HULL_NON_OPTIMIZED(node -> createConvexHullShape(node, false)),
    STATIC_NODE_SHAPE(RigidBodies::createStaticNodeShape);

    private static final float SCALAR = 0.5f;

    private final Function<Node, btCollisionShape> function;

    RigidBodies(Function<Node, btCollisionShape> function) {
        this.function = function;
    }

    // TODO: 5/4/2020 not working
    private static btConvexHullShape createConvexHullShape(Node node, boolean optimize) {
        final Mesh mesh = node.parts.get(0).meshPart.mesh;
        final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;

        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        final btConvexHullShape result = new btConvexHullShape(hull);

        shape.dispose();
        hull.dispose();
        return result;
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

    private static btCollisionShape createStaticNodeShape(Node node) {
        return Bullet.obtainStaticNodeShape(node, false);
    }
}
