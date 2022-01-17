package castle.core.game

import castle.core.common.event.EventQueue
import castle.core.game.config.GameInternalConfig
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.Vector3
import ktx.app.KtxInputAdapter

internal class GameManager(
    private val eventQueue: EventQueue,
    engine: Engine,
    private val gameInternalConfig: GameInternalConfig,
) : KtxInputAdapter {
    companion object {
        const val DEBUG_UI_ENABLE = "DEBUG_UI_ENABLE"
    }

    private val gameUI = gameInternalConfig.gameUI
    private val debugUI = gameInternalConfig.debugUI
    private val temp = Vector3()

    init {
        gameUI.add(engine)
        debugUI.add(engine)
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
        eventQueue.proceed {
            when (it.eventType) {
                DEBUG_UI_ENABLE -> {
                    gameUI.debugEnabled = !gameUI.debugEnabled
                    true
                }
                else -> false
            }
        }
    }

    private fun proceedMessages() {
        val messages = gameUI.chat.pollAllMessages()
        for (message in messages) {
            if (message.contains("debug")) {
                debugUI.isVisible = !debugUI.isVisible
            }
        }
    }

    private fun dispatchAiMessages(delta: Float) {
        GdxAI.getTimepiece().update(delta)
        MessageManager.getInstance().update()
    }
}