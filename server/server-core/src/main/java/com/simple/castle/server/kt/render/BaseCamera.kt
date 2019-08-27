package com.simple.castle.server.kt.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.simple.castle.server.kt.utils.IntersectUtils

class BaseCamera(private val plane: ModelInstance?)
    : PerspectiveCamera(FIELD_OF_VIEW.toFloat(), Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()), InputProcessor {
    companion object {
        private const val FIELD_OF_VIEW = 67
        private const val CAMERA_SPEED = 25f
        private const val NEAR = 1f
        private const val FAR = 300f
    }

    init {
        val startPosition = plane?.transform?.getTranslation(Vector3())
        position[startPosition!!.x + 10f, startPosition.y + 10f] = startPosition.z
        this.lookAt(startPosition)
        near = NEAR
        far = FAR
    }

    private val tempVector = Vector3()
    private var keyUpHolds = false
    private var keyDownHolds = false
    private var keyLeftHolds = false
    private var keyRightHolds = false
    private var mouseDragged = false
    private var previousX = 0f
    private var previousY = 0f

    fun resize(width: Int, height: Int) {
        viewportWidth = width.toFloat()
        viewportHeight = height.toFloat()
    }

    fun update(delta: Float) {
        val cameraSpeed = CAMERA_SPEED * delta
        if (keyUpHolds) {
            position.x = position.x - cameraSpeed
        }
        if (keyDownHolds) {
            position.x = position.x + cameraSpeed
        }
        if (keyRightHolds) {
            position.z = position.z - cameraSpeed
        }
        if (keyLeftHolds) {
            position.z = position.z + cameraSpeed
        }
        super.update()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Input.Keys.UP == keycode || Input.Keys.W == keycode) {
            keyUpHolds = true
        }
        if (Input.Keys.DOWN == keycode || Input.Keys.S == keycode) {
            keyDownHolds = true
        }
        if (Input.Keys.LEFT == keycode || Input.Keys.A == keycode) {
            keyLeftHolds = true
        }
        if (Input.Keys.RIGHT == keycode || Input.Keys.D == keycode) {
            keyRightHolds = true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (Input.Keys.UP == keycode || Input.Keys.W == keycode) {
            keyUpHolds = false
        }
        if (Input.Keys.DOWN == keycode || Input.Keys.S == keycode) {
            keyDownHolds = false
        }
        if (Input.Keys.LEFT == keycode || Input.Keys.A == keycode) {
            keyLeftHolds = false
        }
        if (Input.Keys.RIGHT == keycode || Input.Keys.D == keycode) {
            keyRightHolds = false
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (plane != null && button == Input.Buttons.LEFT) {
            mouseDragged = true
            val vector3 = IntersectUtils.intersectPositionPoint(BoundingBox(), this, plane, screenX, screenY)
            if (vector3 != null) {
                previousX = vector3.x
                previousY = vector3.z
            }
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        mouseDragged = false
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (plane != null && mouseDragged) {
            val vector3 = IntersectUtils.intersectPositionPoint(BoundingBox(), this, plane, screenX, screenY)
            if (vector3 != null) {
                val deltaX = previousX - vector3.x
                val deltaY = previousY - vector3.z
                position.x += deltaX
                position.z += deltaY
            }
        }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        translate(tempVector.set(direction).scl(amount.toFloat()))
        return false
    }
}