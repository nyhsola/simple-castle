package castle.server.ashley.engine

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen

class CustomEngine : PooledEngine(), InputProcessor, Screen {
    private val inputMultiplexer = InputMultiplexer()
    private val screens = ArrayList<Screen>()

    override fun addSystem(system: EntitySystem) {
        if (system is InputProcessor) {
            inputMultiplexer.addProcessor(system)
        }
        if (system is Screen) {
            screens.add(system)
        }
        super.addSystem(system)
    }

    override fun removeSystem(system: EntitySystem) {
        if (system is InputProcessor) {
            inputMultiplexer.removeProcessor(system)
        }
        if (system is Screen) {
            screens.remove(system)
        }
        super.removeSystem(system)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return inputMultiplexer.mouseMoved(screenX, screenY)
    }

    override fun keyTyped(character: Char): Boolean {
        return inputMultiplexer.keyTyped(character)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return inputMultiplexer.scrolled(amountX, amountY)
    }

    override fun keyUp(keycode: Int): Boolean {
        return inputMultiplexer.keyUp(keycode)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer)
    }

    override fun keyDown(keycode: Int): Boolean {
        return inputMultiplexer.keyDown(keycode)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button)
    }

    override fun hide() {
        screens.forEach(Screen::hide)
    }

    override fun show() {
        screens.forEach(Screen::show)
    }

    override fun render(delta: Float) {
        screens.forEach { screen -> screen.render(delta) }
    }

    override fun pause() {
        screens.forEach(Screen::pause)
    }

    override fun resume() {
        screens.forEach(Screen::resume)
    }

    override fun resize(width: Int, height: Int) {
        screens.forEach { screen -> screen.resize(width, height) }
    }

    override fun dispose() {
        screens.forEach(Screen::dispose)
    }

}