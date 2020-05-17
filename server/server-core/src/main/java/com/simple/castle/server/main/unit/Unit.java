package com.simple.castle.server.main.unit;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class Unit {
    protected final btRigidBody body;
    protected final btMotionState motionState;

    public Unit(btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        this.motionState = new btDefaultMotionState();
        this.body = new btRigidBody(constructionInfo);
        this.body.setMotionState(motionState);
    }
}
