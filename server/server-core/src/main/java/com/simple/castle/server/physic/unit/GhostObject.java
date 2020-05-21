package com.simple.castle.server.physic.unit;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class GhostObject extends PhysicObject {
    public GhostObject(final btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(constructionInfo);
        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
    }
}