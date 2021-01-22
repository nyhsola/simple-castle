package castle.server.ashley.service

import castle.server.ashley.game.GameCamera
import castle.server.ashley.systems.adapter.BaseAdapter

class CameraService : BaseAdapter {
    private val index: Int = 0
    private val cameras: MutableList<GameCamera> = mutableListOf(GameCamera())
    var input: Boolean = true
        set(value) {
            cameras[index].resetKeys()
            field = value
        }

    fun getCurrentCamera() = cameras[index]

    fun update(deltaTime: Float) {
        cameras[index].update(deltaTime)
    }

    override fun resize(width: Int, height: Int) {
        cameras[index].resize(width, height)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return if (input) cameras[index].scrolled(amountX, amountY) else false
    }

    override fun keyUp(keycode: Int): Boolean {
        return if (input) cameras[index].keyUp(keycode) else false
    }

    override fun keyDown(keycode: Int): Boolean {
        return if (input) cameras[index].keyDown(keycode) else false
    }
}