package castle.core.common.service

import castle.core.common.`object`.GameCamera
import ktx.app.KtxInputAdapter

class CameraService : KtxInputAdapter {
    private val index: Int = 0
    private val cameras: List<GameCamera> = listOf(GameCamera())
    val currentCamera = cameras[index]

    var input: Boolean = true
        set(value) {
            cameras[index].resetKeys()
            field = value
        }

    fun update(deltaTime: Float) {
        cameras[index].update(deltaTime)
    }

    fun resize(width: Int, height: Int) {
        cameras[index].resize(width, height)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return if (input) cameras[index].scrolled(amountY) else false
    }

    override fun keyUp(keycode: Int): Boolean {
        return if (input) cameras[index].keyUp(keycode) else false
    }

    override fun keyDown(keycode: Int): Boolean {
        return if (input) cameras[index].keyDown(keycode) else false
    }
}