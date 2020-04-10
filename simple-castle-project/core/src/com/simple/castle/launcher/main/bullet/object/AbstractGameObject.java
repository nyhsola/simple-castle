package com.simple.castle.launcher.main.bullet.object;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public abstract class AbstractGameObject extends ModelInstance implements Disposable {
    public final static short GROUND_FLAG = 1 << 8;
    public final static short OBJECT_FLAG = 1 << 9;
    public final static short ALL_FLAG = -1;

    public final btRigidBody body;
    public final MotionState motionState;
    public final String node;

    public AbstractGameObject(GameObjectConstructor gameObjectConstructor) {
        super(gameObjectConstructor.model, new Matrix4(), gameObjectConstructor.node, true);
        this.node = gameObjectConstructor.node;

        motionState = new MotionState();
        motionState.transform = this.transform;

        body = new btRigidBody(gameObjectConstructor.constructionInfo);
        body.setMotionState(motionState);
    }

    @Override
    public void dispose() {
        body.dispose();
        motionState.dispose();
    }

}
