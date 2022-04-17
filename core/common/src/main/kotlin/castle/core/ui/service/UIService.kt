package castle.core.ui.service

import castle.core.event.EventQueue
import castle.core.ui.debug.DebugUI
import castle.core.ui.game.GameUI
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import ktx.app.KtxInputAdapter

class UIService(
        private val eventQueue: EventQueue,
        private val gameUI: GameUI,
        private val debugUI: DebugUI
) : KtxInputAdapter {
    companion object {
        const val DEBUG_UI_ENABLE_1 = "DEBUG_UI_ENABLE_1"
        const val DEBUG_UI_ENABLE_2 = "DEBUG_UI_ENABLE_2"
    }

    fun init(engine: Engine) {
        gameUI.add(engine)
        debugUI.add(engine)
    }

    fun update() {
        gameUI.update()
        debugUI.update()
        proceedEvents()
        proceedMessages()
    }

    fun activateSelection(entity: Entity) {
        gameUI.portrait.changePortrait(entity)
        gameUI.description.updateDescription(entity)
    }

    fun deactivateSelection() {
        gameUI.portrait.resetPortrait()
        gameUI.description.resetDescription()
    }

    private fun proceedEvents() {
        eventQueue.proceed {
            when (it.eventType) {
                DEBUG_UI_ENABLE_1 -> {
                    gameUI.debugEnabled = !gameUI.debugEnabled
                    true
                }
                DEBUG_UI_ENABLE_2 -> {
                    debugUI.debugEnabled = !debugUI.debugEnabled
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
}