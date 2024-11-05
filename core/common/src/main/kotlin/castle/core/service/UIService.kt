package castle.core.service

import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.path.Area
import castle.core.ui.debug.DebugUI
import castle.core.ui.game.GameUI
import castle.core.ui.game.HpHud.HpBar
import castle.core.ui.menu.MenuUI
import castle.core.util.GlobalMode
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter
import org.koin.core.annotation.Single

@Single
class UIService(
    private val engine: Engine,
    private val eventQueue: EventQueue,
    private val gameUI: GameUI,
    private val debugUI: DebugUI,
    private val menuUI: MenuUI
) : KtxInputAdapter, Disposable {
    companion object {
        const val DEBUG_UI_ENABLE_1 = "DEBUG_UI_ENABLE_1"
        const val DEBUG_UI_ENABLE_2 = "DEBUG_UI_ENABLE_2"
        const val MENU_ENABLE = "MENU_ENABLE"
    }

    private val signal = Signal<EventContext>()

    private val operations: Map<String, (EventContext) -> Unit> = mapOf(
        Pair(DEBUG_UI_ENABLE_1) { gameUI.debugEnabled = !gameUI.debugEnabled },
        Pair(DEBUG_UI_ENABLE_2) { debugUI.debugEnabled = !debugUI.debugEnabled },
        Pair(MENU_ENABLE) { menuUI.isVisible = !menuUI.isVisible }
    )

    fun init() {
        signal.add(eventQueue)
        engine.addEntity(gameUI)
        engine.addEntity(menuUI)
        engine.addEntity(debugUI)

        gameUI.init()

        debugUI.isVisible = GlobalMode.debugMode
    }

    fun update(objectsOnMap: Map<Area, Collection<Entity>>) {
        gameUI.update(objectsOnMap)
        debugUI.update()
        eventQueue.proceed(operations)
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

    fun addHp(hpBar: HpBar) {
        gameUI.hpHud.addHp(hpBar)
    }

    fun removeHp(hpBar: HpBar) {
        gameUI.hpHud.removeHp(hpBar)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE) {
            signal.dispatch(EventContext(MENU_ENABLE))
        }
        return false
    }

    private fun proceedMessages() {
        val messages = gameUI.chat.pollAllMessages()
        for (message in messages) {
            if (message.contains("debug")) {
                debugUI.isVisible = !debugUI.isVisible
            }
        }
    }

    override fun dispose() {
        engine.removeEntity(gameUI)
        engine.removeEntity(menuUI)
        engine.removeEntity(debugUI)
    }
}