package com.simple.castle.launcher.physics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class PhysicModel extends ApplicationAdapter {
    private ModelInstance modelInstance;
    private MotionState motionState;

    public PhysicModel(String nodeName, Model model) {
        modelInstance = new ModelInstance(model, nodeName);
        motionState = new MotionState(modelInstance);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public MotionState getMotionState() {
        return motionState;
    }

}
