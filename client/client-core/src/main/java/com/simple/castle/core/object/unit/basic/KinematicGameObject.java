package com.simple.castle.core.object.unit.basic;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.object.unit.add.ObjectConstructor;

public class KinematicGameObject extends AbstractGameObject {
    public KinematicGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor);

        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        this.body.setContactCallbackFlag(GROUND_FLAG);
        this.body.setContactCallbackFilter(0);
        this.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }
}
