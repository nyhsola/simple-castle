package castle.core.common.system

import castle.core.common.service.CameraService
import com.badlogic.ashley.core.EntitySystem
import ktx.app.KtxInputAdapter

class CameraControlSystem(private val cameraService: CameraService) : EntitySystem(), KtxInputAdapter {
    override fun update(deltaTime: Float) {
        cameraService.update(deltaTime)
    }

    override fun keyDown(keycode: Int): Boolean {
        return cameraService.keyDown(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        return cameraService.keyTyped(character)
    }

    override fun keyUp(keycode: Int): Boolean {
        return cameraService.keyUp(keycode)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return cameraService.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return cameraService.scrolled(amountX, amountY)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return cameraService.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return cameraService.touchDragged(screenX, screenY, pointer)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return cameraService.touchUp(screenX, screenY, pointer, button)
    }
}