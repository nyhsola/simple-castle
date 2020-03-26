package com.simple.castle.launcher;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MotionState extends btMotionState {

    private final Vector3 position = new Vector3();
    private final ModelInstance instance;

    public MotionState(ModelInstance instance) {
        this.instance = instance;
    }

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
        worldTrans.set(instance.transform);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        instance.transform.set(worldTrans);
        instance.transform.getTranslation(position);
    }

}
