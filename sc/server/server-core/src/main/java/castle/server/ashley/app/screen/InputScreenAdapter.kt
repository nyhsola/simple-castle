package castle.server.ashley.app.screen

import castle.server.ashley.systems.engine.CustomEngine
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen

abstract class InputScreenAdapter protected constructor() : InputProcessor, Screen {
    protected val customEngine: CustomEngine = CustomEngine()

    override fun keyDown(keycode: Int): Boolean {
        return customEngine.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        return customEngine.keyUp(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        return customEngine.keyTyped(character)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return customEngine.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return customEngine.touchUp(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return customEngine.touchDragged(screenX, screenY, pointer)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return customEngine.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return customEngine.scrolled(amountX, amountY)
    }

    override fun hide() {
        customEngine.hide()
    }

    override fun show() {
        customEngine.show()
    }

    override fun render(delta: Float) {
        customEngine.update(delta)
        customEngine.render(delta)
    }

    override fun pause() {
        customEngine.pause()
    }

    override fun resume() {
        customEngine.resume()
    }

    override fun resize(width: Int, height: Int) {
        customEngine.resize(width, height)
    }

    override fun dispose() {
        customEngine.removeAllEntities()
        customEngine.dispose()
    }
}