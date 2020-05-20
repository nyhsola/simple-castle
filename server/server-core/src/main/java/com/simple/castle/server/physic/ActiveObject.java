package com.simple.castle.server.physic;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

abstract class ActiveObject extends PhysicObject {
    ActiveObject(final btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(constructionInfo);
        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.body.setContactCallbackFlag(Flags.OBJECT_FLAG);
        this.body.setContactCallbackFilter(Flags.GROUND_FLAG);
    }
}
