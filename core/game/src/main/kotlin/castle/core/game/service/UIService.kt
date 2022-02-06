package castle.core.game.service

import castle.core.common.event.EventQueue
import castle.core.common.service.CommonResources
import castle.core.game.config.GameInternalConfig
import castle.core.game.ui.debug.DebugUI
import castle.core.game.ui.game.GameUI
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.app.KtxInputAdapter

class UIService(
    private val eventQueue: EventQueue,
    commonResources: CommonResources,
    private val gameUI: GameUI,
    private val debugUI: DebugUI,
    private val chatService: ChatService
) : KtxInputAdapter {
    companion object {
        const val DEBUG_UI_ENABLE = "DEBUG_UI_ENABLE"
    }

    private val listDrawables: List<String> = listOf("unit-warrior", "castle")
    private val drawables: Map<String, TextureRegionDrawable> = commonResources.textures
        .filter { listDrawables.contains(it.key) }
        .mapValues { TextureRegionDrawable(TextureRegion(it.value)) }

    fun add(engine: Engine) {
        gameUI.add(engine)
        debugUI.add(engine)
    }

    fun update() {
        proceedEvents()
        proceedMessages()
    }

    fun changePortrait(name: String?) {
        if (!drawables.containsKey(name)) {
            gameUI.image.drawable = null
        } else {
            gameUI.image.drawable = drawables[name]
        }
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