package com.simple.castle.launcher.main.bullet.object;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable {
    public final btRigidBody body;
    public final MotionState motionState;
    public final String node;

    public GameObject(Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(model, new Matrix4(), node, true);
        this.node = node;

        motionState = new MotionState();
        motionState.transform = this.transform;

        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);
    }

    @Override
    public void dispose() {
        body.dispose();
        motionState.dispose();
    }

    public String getNode() {
        return node;
    }
}
