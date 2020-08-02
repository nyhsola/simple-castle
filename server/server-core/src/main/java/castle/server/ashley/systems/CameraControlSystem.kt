package castle.server.ashley.systems

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class CameraControlSystem(private val camera: Camera) : SystemAdapter() {
    companion object {
        private const val CAMERA_SPEED = 30f
    }

    private val tempVector = Vector3()

    private var keyLeft = false
    private var keyRight = false
    private var keyDown = false
    private var keyUp = false

    override fun update(deltaTime: Float) {
        if (keyLeft || keyRight || keyDown || keyUp) {
            val cameraSpeed = CAMERA_SPEED * deltaTime
            if (keyLeft) {
                tempVector.set(camera.direction)
                val vector = tempVector.rotate(camera.up, 90f).scl(cameraSpeed)
                camera.position.add(vector.x, 0f, vector.z)
            }
            if (keyRight) {
                tempVector.set(camera.direction)
                val vector = tempVector.rotate(camera.up, -90f).scl(cameraSpeed)
                camera.position.add(vector.x, 0f, vector.z)
            }
            if (keyDown) {
                tempVector.set(camera.direction)
                val vector = tempVector.scl(cameraSpeed)
                camera.position.sub(vector.x, 0f, vector.z)
            }
            if (keyUp) {
                tempVector.set(camera.direction)
                val vector = tempVector.scl(cameraSpeed)
                camera.position.add(vector.x, 0f, vector.z)
            }
            camera.update()
        }
    }

    override fun resize(width: Int, height: Int) {
        camera.apply {
            viewportHeight = height.toFloat()
            viewportWidth = width.toFloat()
        }
        camera.update()
    }

    override fun scrolled(amount: Int): Boolean {
        camera.translate(tempVector.set(camera.direction).scl(amount.toFloat()))
        camera.update()
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> keyLeft = false
            Input.Keys.D, Input.Keys.RIGHT -> keyRight = false
            Input.Keys.S, Input.Keys.DOWN -> keyDown = false
            Input.Keys.W, Input.Keys.UP -> keyUp = false
        }
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> keyLeft = true
            Input.Keys.D, Input.Keys.R -> keyRight = true
            Input.Keys.S, Input.Keys.DOWN -> keyDown = true
            Input.Keys.W, Input.Keys.UP -> keyUp = true
        }
        return false
    }

}