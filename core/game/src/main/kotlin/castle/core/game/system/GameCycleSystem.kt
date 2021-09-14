package castle.core.game.system

import castle.core.game.GameManager
import castle.core.game.config.GameInternalConfig
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

internal class GameCycleSystem(
    private val gameInternalConfig: GameInternalConfig
) : IntervalSystem(GAME_TICK), KtxInputAdapter, Disposable {
    private companion object {
        const val GAME_TICK: Float = 0.1f
    }

    private lateinit var gameManager: GameManager

    override fun addedToEngine(engine: Engine) {
        gameManager = GameManager(engine, gameInternalConfig)
    }

    override fun updateInterval() {
        gameManager.update(GAME_TICK)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return gameManager.touchDown(screenX, screenY, pointer, button)
    }

    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.ESCAPE -> {
                Gdx.app.exit()
                true
            }
            else -> super.keyDown(keycode)
        }
    }

    override fun dispose() {
        gameManager.dispose()
    }
}