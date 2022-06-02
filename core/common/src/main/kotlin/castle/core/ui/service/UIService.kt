package castle.core.ui.service

import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.ui.debug.DebugUI
import castle.core.ui.game.GameUI
import castle.core.ui.menu.MenuUI
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Input
import ktx.app.KtxInputAdapter

class UIService(
    private val eventQueue: EventQueue,
    private val gameUI: GameUI,
    private val menuUI: MenuUI,
    private val debugUI: DebugUI
) : KtxInputAdapter {
    companion object {
        const val DEBUG_UI_ENABLE_1 = "DEBUG_UI_ENABLE_1"
        const val DEBUG_UI_ENABLE_2 = "DEBUG_UI_ENABLE_2"
        const val MENU_ENABLE = "MENU_ENABLE"
    }

    private val signal = Signal<EventContext>()

    fun init(engine: Engine) {
        signal.add(eventQueue)
        engine.addEntity(gameUI)
        engine.addEntity(menuUI)
        engine.addEntity(debugUI)
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

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE) {
            signal.dispatch(EventContext(MENU_ENABLE))
        }
        return super.keyDown(keycode)
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
                MENU_ENABLE -> {
                    menuUI.isVisible = !menuUI.isVisible
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