package com.simple.castle.server.kt.composition

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.simple.castle.server.kt.physic.ActiveObject
import com.simple.castle.server.kt.physic.KinematicObject
import com.simple.castle.server.kt.physic.PhysicObject
import java.util.function.Function

enum class InteractType(private val function: Function<btRigidBody.btRigidBodyConstructionInfo, PhysicObject>) {
    ACTIVE(Function<btRigidBody.btRigidBodyConstructionInfo, PhysicObject>
    { constructionInfo: btRigidBody.btRigidBodyConstructionInfo? -> ActiveObject(constructionInfo!!) }),
    KINEMATIC(Function<btRigidBody.btRigidBodyConstructionInfo, PhysicObject>
    { constructionInfo: btRigidBody.btRigidBodyConstructionInfo? -> KinematicObject(constructionInfo!!) }),
    GHOST(Function<btRigidBody.btRigidBodyConstructionInfo, PhysicObject>
    { constructionInfo: btRigidBody.btRigidBodyConstructionInfo? -> KinematicObject(constructionInfo!!) });

    fun build(info: btRigidBody.btRigidBodyConstructionInfo): PhysicObject {
        return function.apply(info)
    }

}