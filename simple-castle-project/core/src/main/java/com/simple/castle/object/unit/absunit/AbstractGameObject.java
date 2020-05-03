package com.simple.castle.object.unit.absunit;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.object.constructors.ObjectConstructor;

public abstract class AbstractGameObject extends ModelInstance implements Disposable {
    public final static short GROUND_FLAG = 1 << 8;
    public final static short OBJECT_FLAG = 1 << 9;
    public final static short ALL_FLAG = -1;

    public final btRigidBody body;
    public final MotionState motionState;

    public final String name;

    public AbstractGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor.model, objectConstructor.node, true);

        motionState = new MotionState();
        motionState.transform = this.transform;

        name = objectConstructor.node;

        body = new btRigidBody(objectConstructor.constructionInfo);
        body.setMotionState(motionState);
        body.userData = name;
    }

    @Override
    public void dispose() {
        body.dispose();
        motionState.dispose();
    }

}
