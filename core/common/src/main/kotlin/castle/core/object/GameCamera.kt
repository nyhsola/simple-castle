package castle.core.`object`

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector3

class GameCamera {
    companion object {
        private const val CAMERA_SPEED = 30f
    }

    val camera: Camera = PerspectiveCamera().apply {
        near = 1f
        far = 300f
        fieldOfView = 67f
        viewportWidth = Gdx.graphics.width.toFloat()
        viewportHeight = Gdx.graphics.height.toFloat()
        position.set(Vector3(25f, 25f, 0f))
        lookAt(Vector3.Zero)
        update()
    }

    val environment: Environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1f))
        add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    private val tempVector = Vector3()
    private var keyLeft = false
    private var keyRight = false
    private var keyDown = false
    private var keyUp = false

    fun update(deltaTime: Float) {
        if (keyLeft || keyRight || keyDown || keyUp) {
            val cameraSpeed = CAMERA_SPEED * deltaTime
            if (keyLeft) {
                tempVector.set(camera.direction).rotate(camera.up, 90f).scl(cameraSpeed)
                camera.position.add(tempVector.x, 0f, tempVector.z)
            }
            if (keyRight) {
                tempVector.set(camera.direction).rotate(camera.up, -90f).scl(cameraSpeed)
                camera.position.add(tempVector.x, 0f, tempVector.z)
            }
            if (keyDown) {
                tempVector.set(camera.direction).scl(cameraSpeed)
                camera.position.sub(tempVector.x, 0f, tempVector.z)
            }
            if (keyUp) {
                tempVector.set(camera.direction).scl(cameraSpeed)
                camera.position.add(tempVector.x, 0f, tempVector.z)
            }
            camera.update()
        }
    }

    fun resize(width: Int, height: Int) {
        camera.apply {
            viewportHeight = height.toFloat()
            viewportWidth = width.toFloat()
        }
        camera.update()
    }

    fun scrolled(amountY: Float): Boolean {
        camera.translate(tempVector.set(camera.direction).scl(amountY))
        camera.update()
        return false
    }

    fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> keyLeft = false
            Input.Keys.D, Input.Keys.RIGHT -> keyRight = false
            Input.Keys.S, Input.Keys.DOWN -> keyDown = false
            Input.Keys.W, Input.Keys.UP -> keyUp = false
        }
        return false
    }

    fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> keyLeft = true
            Input.Keys.D, Input.Keys.RIGHT -> keyRight = true
            Input.Keys.S, Input.Keys.DOWN -> keyDown = true
            Input.Keys.W, Input.Keys.UP -> keyUp = true
        }
        return false
    }

    fun resetKeys() {
        keyUp = false
        keyDown = false
        keyLeft = false
        keyRight = false
    }
}