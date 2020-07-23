package com.simple.castle.server.kt.physic

import com.badlogic.gdx.physics.bullet.collision.Collision
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

open class KinematicObject(constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : PhysicObject(constructionInfo) {
    init {
        body.collisionFlags = body.collisionFlags or btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT
        body.contactCallbackFlag = Flags.GROUND_FLAG.toInt()
        body.contactCallbackFilter = 0
        body.activationState = Collision.DISABLE_DEACTIVATION
    }
}