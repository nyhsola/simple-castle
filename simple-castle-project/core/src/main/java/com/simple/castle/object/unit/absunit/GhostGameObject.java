package com.simple.castle.object.unit.absunit;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.object.constructors.ObjectConstructor;

public class GhostGameObject extends AbstractGameObject {
    public GhostGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor);
        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
    }
}
