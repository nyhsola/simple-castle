package castle.server.ashley.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen

class CustomEngine : PooledEngine(), InputProcessor, Screen {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.touchUp(screenX, screenY, pointer, button)
            }
        }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.mouseMoved(screenX, screenY)
            }
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.keyTyped(character)
            }
        }
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.scrolled(amount)
            }
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.keyUp(keycode)
            }
        }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.touchDragged(screenX, screenY, pointer)
            }
        }
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.keyDown(keycode)
            }
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        for (system in systems) {
            if (system is InputProcessor) {
                system.touchDown(screenX, screenY, pointer, button)
            }
        }
        return false
    }

    override fun hide() {
        for (system in systems) {
            if (system is Screen) {
                system.hide()
            }
        }
    }

    override fun show() {
        for (system in systems) {
            if (system is Screen) {
                system.show()
            }
        }
    }

    override fun render(delta: Float) {
        for (system in systems) {
            if (system is Screen) {
                system.render(delta)
            }
        }
    }

    override fun pause() {
        for (system in systems) {
            if (system is Screen) {
                system.pause()
            }
        }
    }

    override fun resume() {
        for (system in systems) {
            if (system is Screen) {
                system.resume()
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        for (system in systems) {
            if (system is Screen) {
                system.resize(width, height)
            }
        }
    }

    override fun dispose() {
        for (system in systems) {
            if (system is Screen) {
                system.dispose()
            }
        }
    }

}