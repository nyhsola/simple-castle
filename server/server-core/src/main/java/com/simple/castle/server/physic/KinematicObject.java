package com.simple.castle.server.physic;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

abstract class KinematicObject extends PhysicObject {
    KinematicObject(btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(constructionInfo);

        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        this.body.setContactCallbackFlag(Flags.GROUND_FLAG);
        this.body.setContactCallbackFilter(0);
        this.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }

}
