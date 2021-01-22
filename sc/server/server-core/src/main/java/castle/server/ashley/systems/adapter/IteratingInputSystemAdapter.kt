package castle.server.ashley.systems.adapter

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor

abstract class IteratingInputSystemAdapter(family: Family?) : IteratingSystemAdapter(family) {
    private val inputMultiplexer = InputMultiplexer()

    protected fun addInputProcessor(inputProcessor: InputProcessor) {
        inputMultiplexer.addProcessor(inputProcessor)
    }

    protected fun removeInputProcessor(inputProcessor: InputProcessor) {
        inputMultiplexer.removeProcessor(inputProcessor)
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

    override fun processEntity(entity: Entity?, deltaTime: Float) {

    }
}