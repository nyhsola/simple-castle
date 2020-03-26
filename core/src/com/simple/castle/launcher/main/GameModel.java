package com.simple.castle.launcher.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.simple.castle.launcher.physics.MotionState;

public class GameModel extends ApplicationAdapter {
    private ModelInstance modelInstance;
    private MotionState motionState;

    public GameModel(String nodeName, Model model) {
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
