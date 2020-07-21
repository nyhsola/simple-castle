package com.simple.castle.server.kt.controller

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.simple.castle.server.kt.composition.BaseObject
import com.simple.castle.server.kt.composition.Constructor

class PlayerUnit(constructor: Constructor?, initPosition: Vector3?, val playerName: String) : BaseObject(constructor!!) {
    private val tempVector = Vector3()
    private var movePoint: Vector3? = null
    private var previousAngle = 0.0
    private var rotateDirection = false
    var isDead = false

    companion object {
        private const val DEFAULT_SPEED_ROTATION = 3
        private const val DEFAULT_SPEED_MOVEMENT = 5
        private const val DEFAULT_SPEED_MOVEMENT_ON_ROTATION = 5
        private val ANGULAR_FACTOR = Vector3(0.0f, 1.0f, 0.0f)
        private val FACE_DIRECTION = Vector3(1.0f, 0.0f, 0.0f)
        private val ROTATE_LEFT = Vector3.Y.cpy().scl(DEFAULT_SPEED_ROTATION.toFloat())
        private val ROTATE_RIGHT = Vector3.Y.cpy().scl(-DEFAULT_SPEED_ROTATION.toFloat())
    }

    init {
        physicObject!!.body.worldTransform = Matrix4()
        physicObject.body.translate(initPosition)
        physicObject.body.angularFactor = ANGULAR_FACTOR
    }

    fun update() {
        if (movePoint != null) {
            val linearVelocity = linearVelocity
            val angularVelocity = getAngularVelocity(movePoint!!, linearVelocity)
            if (Vector3.Zero == angularVelocity) {
                linearVelocity.scl(DEFAULT_SPEED_MOVEMENT.toFloat())
            } else {
                linearVelocity.scl(DEFAULT_SPEED_MOVEMENT_ON_ROTATION.toFloat())
            }
            physicObject!!.body.linearVelocity = linearVelocity
            physicObject.body.angularVelocity = angularVelocity
        } else {
            physicObject!!.body.linearVelocity = Vector3.Zero
            physicObject.body.angularVelocity = Vector3.Zero
        }
    }

    private fun getAngularVelocity(target: Vector3, linearVelocity: Vector3): Vector3 {
        var angularVelocity = Vector3.Zero
        val unitPosition = modelInstance!!.transform.getTranslation(tempVector)
        val targetDirection = target.cpy().sub(unitPosition).nor()
        val currentAngle = getAngle(targetDirection, linearVelocity)
        if (currentAngle <= 0 || currentAngle >= 10) {
            if (currentAngle - previousAngle > 0) {
                rotateDirection = !rotateDirection
            }
            angularVelocity = if (rotateDirection) {
                ROTATE_LEFT
            } else {
                ROTATE_RIGHT
            }
            previousAngle = currentAngle
        }
        return angularVelocity
    }

    private val linearVelocity: Vector3
        private get() = physicObject!!.body.orientation.transform(FACE_DIRECTION.cpy())

    fun setMovePoint(movePoint: Vector3?) {
        this.movePoint = movePoint
    }

    private fun getAngle(a: Vector3, b: Vector3): Double {
        val norA = b.cpy().nor()
        val norB = a.cpy().nor()
        val dot = norB.dot(norA)
        return Math.toDegrees(Math.acos(dot.toDouble()))
    }

}