package com.simple.castle.core.kt.screen

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.ScreenAdapter

abstract class BaseScreen protected constructor() : ScreenAdapter(), InputProcessor {
    protected val inputMultiplexer: InputMultiplexer = InputMultiplexer()

    override fun keyDown(keycode: Int): Boolean {
        return inputMultiplexer.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        return inputMultiplexer.keyUp(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        return inputMultiplexer.keyTyped(character)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return inputMultiplexer.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amount: Int): Boolean {
        return inputMultiplexer.scrolled(amount)
    }

}