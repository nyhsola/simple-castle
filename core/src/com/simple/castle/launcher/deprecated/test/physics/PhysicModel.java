package com.simple.castle.launcher.deprecated.test.physics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class PhysicModel extends ApplicationAdapter {
    private ModelInstance modelInstance;
    private com.simple.castle.launcher.deprecated.test.physics.MotionState motionState;

    public PhysicModel(String nodeName, Model model) {
        modelInstance = new ModelInstance(model, nodeName);
        motionState = new com.simple.castle.launcher.deprecated.test.physics.MotionState(modelInstance);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public MotionState getMotionState() {
        return motionState;
    }

}
