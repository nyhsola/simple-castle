package com.simple.castle.server.kt.composition

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState

class MotionState : btMotionState() {
    var transform: Matrix4? = null
    override fun getWorldTransform(worldTrans: Matrix4) {
        worldTrans.set(transform)
    }

    override fun setWorldTransform(worldTrans: Matrix4) {
        transform!!.set(worldTrans)
    }
}