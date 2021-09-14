package castle.core.game

import castle.core.game.`object`.GameContext
import castle.core.game.`object`.Players
import castle.core.game.config.GameInternalConfig
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter

internal class GameManager(
    engine: Engine,
    private val gameInternalConfig: GameInternalConfig,
) : KtxInputAdapter, Disposable {
    private val gameContext: GameContext = GameContext(engine, gameInternalConfig.gameEnvironment, gameInternalConfig.gameResourceService)
    private val eventQueue = EventQueue()

    private val gameUI = gameInternalConfig.gameUI
    private val players: Players = Players(gameContext)

    private val temp = Vector3()

    init {
        gameUI.addListener(eventQueue)
        gameUI.add(engine)
    }

    fun update(delta: Float) {
        proceedEvents()
        proceedMessages()
        dispatchAiMessages(delta)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val collisionObject = gameInternalConfig.rayCastService.rayCast(screenX.toFloat(), screenY.toFloat())
        if (collisionObject != null) {
            collisionObject.worldTransform.getTranslation(temp)

            val nodeStr = collisionObject.userData as String
            val position = "X: ${temp.x}\nY:${temp.y}\nZ: ${temp.z}"

            gameUI.chat.typeMessage("$nodeStr\n$position")
        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    private fun proceedEvents() {
        val gameEvents = eventQueue.pollAll()
        for (gameEvent in gameEvents) {
            when (gameEvent.eventType) {
                EventType.CHAT_FOCUSED -> {
                    gameInternalConfig.commonConfig.cameraService.input = false
                }
                EventType.CHAT_UNFOCUSED -> {
                    gameInternalConfig.commonConfig.cameraService.input = true
                }
                EventType.UI1_BUTTON_CLICK -> {
                    players.spawn()
                }
            }
        }
    }

    private fun proceedMessages() {
        val messages = gameUI.chat.pollAllMessages()
        for (message in messages) {
            if (message.contains("debug-physic")) {
                gameInternalConfig.physicConfig.physicService.debugEnabled = !gameInternalConfig.physicConfig.physicService.debugEnabled
            }
            if (message.contains("debug-ui")) {
                gameUI.debugEnabled = !gameUI.debugEnabled
            }
        }
    }

    private fun dispatchAiMessages(delta: Float) {
        GdxAI.getTimepiece().update(delta)
        MessageManager.getInstance().update()
    }

    override fun dispose() {
        players.dispose()
        gameContext.dispose()
    }
}