package com.simple.castle.object.unit.basic;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.object.unit.add.ObjectConstructor;

public class GhostGameObject extends AbstractGameObject {
    public GhostGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor);
        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
    }
}