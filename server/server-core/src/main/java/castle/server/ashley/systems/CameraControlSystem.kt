package castle.server.ashley.systems

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class CameraControlSystem(private val camera: Camera) : SystemAdapter() {
    companion object {
        private const val CAMERA_SPEED = 25f
    }

    private val tempVector = Vector3()
    private var currentKey = 0
    private var mouseDragged = false

    override fun update(deltaTime: Float) {
        val cameraSpeed = CAMERA_SPEED * deltaTime
        when (currentKey) {
            Input.Keys.UP, Input.Keys.W -> camera.position.x = camera.position.x - cameraSpeed
            Input.Keys.DOWN, Input.Keys.S -> camera.position.x = camera.position.x + cameraSpeed
            Input.Keys.LEFT, Input.Keys.A -> camera.position.z = camera.position.z + cameraSpeed
            Input.Keys.RIGHT, Input.Keys.D -> camera.position.z = camera.position.z - cameraSpeed
            Input.Keys.Q -> camera.rotateAround(Vector3.Zero, Vector3(0f, 0f, 1f), 1f)
            Input.Keys.E -> camera.rotateAround(Vector3.Zero, Vector3(0f, 0f, 1f), -1f)
            Input.Keys.SHIFT_LEFT -> camera.position.add(camera.up)
            Input.Keys.CONTROL_LEFT -> camera.position.sub(camera.up)
        }
        camera.update()
    }

    override fun resize(width: Int, height: Int) {
        camera.apply {
            viewportHeight = height.toFloat()
            viewportWidth = width.toFloat()
        }
        camera.update()
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//        mouseDragged = false
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        camera.translate(tempVector.set(camera.direction).scl(amount.toFloat()))
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        currentKey = Input.Keys.ANY_KEY
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
//        if (mouseDragged) {
//            val vector3 = IntersectUtils.intersectPositionPoint(BoundingBox(), camera, ground, screenX, screenY)
//            if (vector3 != null) {
//                val deltaX = previousX - vector3.x
//                val deltaY = previousY - vector3.z
//                camera.position.x += deltaX
//                camera.position.z += deltaY
//            }
//        }
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        currentKey = keycode
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//        if (button == Input.Buttons.LEFT) {
//            mouseDragged = true
//            val vector3 = IntersectUtils.intersectPositionPoint(BoundingBox(), camera, ground, screenX, screenY)
//            if (vector3 != null) {
//                previousX = vector3.x
//                previousY = vector3.z
//            }
//        }
        return false
    }

}