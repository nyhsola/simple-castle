package castle.core.ui.service

import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.path.Area
import castle.core.service.CommonResources
import castle.core.service.MapScanService
import castle.core.ui.debug.DebugUI
import castle.core.ui.game.GameUI
import castle.core.ui.menu.MenuUI
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxInputAdapter

class UIService(
    private val engine: Engine,
    private val eventQueue: EventQueue,
    mapScanService: MapScanService,
    commonResources: CommonResources,
    shapeRenderer: ShapeRenderer,
    private val spriteBatch: SpriteBatch,
    private val viewport: Viewport
) : KtxInputAdapter, Disposable {
    companion object {
        const val DEBUG_UI_ENABLE_1 = "DEBUG_UI_ENABLE_1"
        const val DEBUG_UI_ENABLE_2 = "DEBUG_UI_ENABLE_2"
        const val MENU_ENABLE = "MENU_ENABLE"
    }

    private val gameUI = GameUI(createStage(), mapScanService, commonResources, shapeRenderer, eventQueue)
    private val debugUI = DebugUI(createStage(), commonResources, eventQueue)
    private val menuUI = MenuUI(createStage(), commonResources, eventQueue)
    private val signal = Signal<EventContext>()

    private fun createStage() = Stage(viewport, spriteBatch)

    fun init() {
        signal.add(eventQueue)
        engine.addEntity(gameUI)
        engine.addEntity(menuUI)
        engine.addEntity(debugUI)

        gameUI.init()
    }

    fun update(objectsOnMap: Map<Area, Collection<Entity>>) {
        gameUI.update(objectsOnMap)
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

    override fun dispose() {
        engine.removeEntity(gameUI)
        engine.removeEntity(menuUI)
        engine.removeEntity(debugUI)
    }
}