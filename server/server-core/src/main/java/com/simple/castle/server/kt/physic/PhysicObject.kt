package com.simple.castle.server.kt.physic

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.server.kt.composition.MotionState

abstract class PhysicObject internal constructor(private val constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : Disposable {
    val body: btRigidBody = btRigidBody(constructionInfo)
    val motionState: MotionState = MotionState()

    init {
        body.userData = this
    }

    override fun dispose() {
        motionState.dispose()
        constructionInfo.dispose()
        body.dispose()
    }
}