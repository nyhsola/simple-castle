package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class GameModel extends ApplicationAdapter {
    private ModelInstance modelInstance;
    private MotionState motionState;
    private btRigidBody btRigidBody;

    public GameModel(String nodeName, Model model) {
        modelInstance = new ModelInstance(model, nodeName);
        motionState = new MotionState(modelInstance.transform);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public MotionState getMotionState() {
        return motionState;
    }

    public com.badlogic.gdx.physics.bullet.dynamics.btRigidBody getBtRigidBody() {
        return btRigidBody;
    }

    public void setBtRigidBody(com.badlogic.gdx.physics.bullet.dynamics.btRigidBody btRigidBody) {
        this.btRigidBody = btRigidBody;
    }
}
