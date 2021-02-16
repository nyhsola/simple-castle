package castle.server.ashley.service

import castle.server.ashley.app.creator.GUICreator
import castle.server.ashley.game.*
import castle.server.ashley.game.event.EventContext
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.utils.Disposable

class GameManager(
    private val engine: Engine,
    private val resourceManager: ResourceManager,
    guiCreator: GUICreator,
    private val mapService: MapService,
    signal: Signal<EventContext>
) : Disposable {
    private enum class Initialize {
        STEP_1, STEP_2
    }

    private var initializeStep = 0

    private lateinit var gameMap: GameMap
    private lateinit var minimap: Minimap
    private lateinit var gameEnvironment: GameEnvironment
    private lateinit var gameContext: GameContext
    private lateinit var players: Players

    val chat = Chat(engine, guiCreator, signal, resourceManager)

    fun update(delta: Float) {
        val size = Initialize.values().size
        if (initializeStep < size) {
            when (Initialize.values()[initializeStep]) {
                Initialize.STEP_1 -> {
                    initializationStep1()
                }
                Initialize.STEP_2 -> {
                    initializationStep2()
                }
            }
            initializeStep++
        } else {
            players.update(delta)
        }
    }

    override fun dispose() {
        gameEnvironment.dispose()
        chat.dispose()
        players.dispose()
    }

    private fun initializationStep1() {
        gameEnvironment = GameEnvironment(engine, resourceManager)
    }

    private fun initializationStep2() {
        gameMap = GameMap(mapService, resourceManager)
        minimap = Minimap(engine, gameMap)
        gameContext = GameContext(engine, resourceManager, chat, gameMap)
        players = Players(gameContext)
    }
}