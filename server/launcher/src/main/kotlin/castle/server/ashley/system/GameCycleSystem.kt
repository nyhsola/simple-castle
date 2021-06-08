package castle.server.ashley.system

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.game.GameManager
import castle.server.ashley.system.service.CameraService
import castle.server.ashley.system.service.PhysicService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

class GameCycleSystem(
    private val guiCreator: GUICreator,
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : IntervalSystem(GAME_TICK), KtxInputAdapter, Disposable {
    private companion object {
        const val GAME_TICK: Float = 0.1f
    }

    private lateinit var gameManager: GameManager

    override fun addedToEngine(engine: Engine) {
        gameManager = GameManager(engine, guiCreator, physicService, cameraService)
    }

    override fun updateInterval() {
        gameManager.update(GAME_TICK)
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