package com.simple.castle.server.kt.physic

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState
import com.badlogic.gdx.utils.Disposable

abstract class PhysicObject internal constructor(private val constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : Disposable {
    val body: btRigidBody
    val motionState: btMotionState
    override fun dispose() {
        motionState.dispose()
        constructionInfo.dispose()
        body.dispose()
    }

    init {
        motionState = btDefaultMotionState()
        body = btRigidBody(constructionInfo)
        body.motionState = motionState
        body.userData = this
    }
}