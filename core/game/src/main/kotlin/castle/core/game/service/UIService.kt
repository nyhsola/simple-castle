package castle.core.game.service

import castle.core.common.event.EventQueue
import castle.core.game.config.GameInternalConfig
import castle.core.game.ui.debug.DebugUI
import castle.core.game.ui.game.GameUI
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector3
import ktx.app.KtxInputAdapter

class UIService(
    private val eventQueue: EventQueue,
    private val gameUI: GameUI,
    private val debugUI: DebugUI,
    private val chatService: ChatService
) : KtxInputAdapter {
    companion object {
        const val DEBUG_UI_ENABLE = "DEBUG_UI_ENABLE"
    }

    fun add(engine: Engine) {
        gameUI.add(engine)
        debugUI.add(engine)
    }

    fun update() {
        proceedEvents()
        proceedMessages()
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
        val messages = chatService.pollAllMessages()
        for (message in messages) {
            if (message.contains("debug")) {
                debugUI.isVisible = !debugUI.isVisible
            }
        }
    }
}