package com.simple.castle.launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.bullet.collision.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Tools {
    public static btConvexHullShape createConvexHullShape(final Model model, boolean optimize) {
        final Mesh mesh = model.meshes.get(0);
        final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;
        // now optimize the shape
        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        final btConvexHullShape result = new btConvexHullShape(hull);
        // delete the temporary shape
        shape.dispose();
        hull.dispose();
        return result;
    }

    private FloatBuffer getFloatBuffer(Node model) {
        MeshPart meshPart = model.parts.get(0).meshPart;

        float[] nVerts = new float[meshPart.size];
        meshPart.mesh.getVertices(nVerts);

        ByteBuffer allocate = ByteBuffer.allocateDirect(nVerts.length * 4);
        allocate.order(ByteOrder.nativeOrder());

        FloatBuffer floatBuffer1 = allocate.asFloatBuffer();
        floatBuffer1.put(nVerts);
        floatBuffer1.position(0);

        return floatBuffer1;
    }

    boolean checkCollision(btDispatcher dispatcher, btCollisionObject one, btCollisionObject two) {
        CollisionObjectWrapper oneWrapper = new CollisionObjectWrapper(one);
        CollisionObjectWrapper twoWrapper = new CollisionObjectWrapper(two);

        btCollisionAlgorithmConstructionInfo constructionInfo = new btCollisionAlgorithmConstructionInfo();
        constructionInfo.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, constructionInfo, oneWrapper.wrapper, twoWrapper.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(oneWrapper.wrapper, twoWrapper.wrapper);

        algorithm.processCollision(oneWrapper.wrapper, twoWrapper.wrapper, info, result);

        int numContacts = result.getPersistentManifold().getNumContacts();
        boolean r = numContacts > 0;

        Gdx.app.log("tag", "" + numContacts);

        result.dispose();
        info.dispose();
        algorithm.dispose();
        constructionInfo.dispose();
        twoWrapper.dispose();
        oneWrapper.dispose();

        return r;
    }
}
