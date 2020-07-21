package com.simple.castle.server.kt.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

class ActiveObject(constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : PhysicObject(constructionInfo) {
    init {
        body.collisionFlags = body.collisionFlags or btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
        body.contactCallbackFlag = Flags.OBJECT_FLAG.toInt()
        body.contactCallbackFilter = Flags.GROUND_FLAG.toInt()
    }
}