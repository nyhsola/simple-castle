package com.simple.castle.launcher.main.bullet.object;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class GameObjectConstructor implements Disposable {
    private static Vector3 localInertia = new Vector3();
    public final Model model;
    public final String node;
    public final btCollisionShape shape;
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;

    public GameObjectConstructor(Model model, String node, btCollisionShape shape, float mass) {
        this.model = model;
        this.node = node;
        this.shape = shape;
        if (mass > 0f) {
            shape.calculateLocalInertia(mass, localInertia);
        } else {
            localInertia.set(0, 0, 0);
        }
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
    }

    @Override
    public void dispose() {
        shape.dispose();
        constructionInfo.dispose();
    }
}
