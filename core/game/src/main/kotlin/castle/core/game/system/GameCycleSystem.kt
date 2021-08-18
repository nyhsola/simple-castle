package castle.core.game.system

import castle.core.common.creator.GUIConfig
import castle.core.common.service.CameraService
import castle.core.game.GameManager
import castle.core.common.service.PhysicService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

class GameCycleSystem(
    private val guiConfig: GUIConfig,
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : IntervalSystem(GAME_TICK), KtxInputAdapter, Disposable {
    private companion object {
        const val GAME_TICK: Float = 0.1f
    }

    private lateinit var gameManager: GameManager

    override fun addedToEngine(engine: Engine) {
        gameManager = GameManager(engine, guiConfig, physicService, cameraService)
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