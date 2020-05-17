package com.simple.castle.server.game.core.object.unit.abs;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.server.game.core.object.unit.add.MotionState;
import com.simple.castle.server.game.core.object.unit.add.ObjectConstructor;

public abstract class AbstractGameObject extends ModelInstance implements Disposable {
    public final static short GROUND_FLAG = 1 << 8;
    public final static short OBJECT_FLAG = 1 << 9;
    public final static short ALL_FLAG = -1;

    public final btRigidBody body;
    public final MotionState motionState;

    public boolean hide = false;

    public AbstractGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor.model, objectConstructor.node, true);

        motionState = new MotionState();
        motionState.transform = this.transform;

        body = new btRigidBody(objectConstructor.constructionInfo);
        body.setMotionState(motionState);
        body.userData = this;
    }

    @Override
    public void dispose() {
        body.dispose();
        motionState.dispose();
    }


    public String getNodeName() {
        return this.nodes.get(0).id;
    }

}
