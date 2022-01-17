package castle.core.game.ui.debug

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.common.event.EventContext
import castle.core.common.event.EventQueue
import castle.core.common.service.CommonResources
import castle.core.common.system.PhysicSystem
import castle.core.game.GameManager
import castle.core.game.system.PlayerSystem
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align

class DebugUI(
    private val commonResources: CommonResources,
    guiConfig: GUIConfig,
    eventQueue: EventQueue
) : CommonEntity() {
    private val stageComponent: StageComponent = StageComponent(guiConfig.createStage()).also { this.add(it) }
    private val signal = Signal<EventContext>()
    private val rootContainer = Container<Table>()
    private val rootTable = Table()

    var isVisible: Boolean = false
        set(value) {
            rootContainer.isVisible = value
            field = value
        }

    init {
        signal.add(eventQueue)
        rootContainer.isVisible = false
        rootTable.add(tab()).grow()
        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = rootTable
        stageComponent.stage.addActor(rootContainer)
    }

    private fun tab(): Container<Table> {
        val containerAbility = Container<Table>().align(Align.topRight)
        val table = Table()
        table.add(createButton("Physic", PhysicSystem.PHYSIC_ENABLE)).width(50f).height(50f)
        table.row()
        table.add(createButton("UI", GameManager.DEBUG_UI_ENABLE)).width(50f).height(50f)
        table.row()
        val list = listOf(
            mapOf(Pair("Player 1", "SPAWN")),
            mapOf(Pair("Player 2", "SPAWN"))
        )
        table.add(createButton("Spawn", PlayerSystem.SPAWN, list)).width(50f).height(50f)
        containerAbility.actor = table
        return containerAbility
    }

    private fun createButton(text: String, eventType: String, map: List<Map<String, String>> = ArrayList()): TextButton {
        val button = TextButton(text, commonResources.skin)
        button.color = Color.DARK_GRAY
        button.color.a = 0.5f
        button.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                map.forEach { signal.dispatch(EventContext(eventType, it)) }
            }
        })
        return button
    }
}