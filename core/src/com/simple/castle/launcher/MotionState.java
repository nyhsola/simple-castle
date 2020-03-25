package com.simple.castle.launcher;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MotionState extends btMotionState {

    private Matrix4 transform;

    public MotionState(Matrix4 transform) {
        this.transform = transform;
    }

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
        transform.set(worldTrans);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        worldTrans.set(transform);
    }

    public Matrix4 getTransform() {
        return transform;
    }
}
