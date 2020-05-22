package com.simple.castle.server.physic.unit;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

public abstract class PhysicObject implements Disposable {
    private final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    protected final btRigidBody body;
    protected final btMotionState motionState;

    PhysicObject(final btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        this.constructionInfo = constructionInfo;
        this.motionState = new btDefaultMotionState();
        this.body = new btRigidBody(constructionInfo);
        this.body.setMotionState(motionState);

        this.body.userData = this;
    }

    @Override
    public void dispose() {
        motionState.dispose();
        constructionInfo.dispose();
        body.dispose();
    }

    public btRigidBody getBody() {
        return body;
    }

    public btMotionState getMotionState() {
        return motionState;
    }
}
