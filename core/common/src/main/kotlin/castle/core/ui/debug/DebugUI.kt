package castle.core.ui.debug

import castle.core.`object`.CommonEntity
import castle.core.component.render.StageRenderComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.service.CommonResources
import castle.core.service.MapService
import castle.core.service.PlayerService
import castle.core.system.PhysicSystem
import castle.core.system.UnitSystem
import castle.core.ui.service.UIService
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align

class DebugUI(
        private val commonResources: CommonResources,
        stage: Stage,
        eventQueue: EventQueue
) : CommonEntity() {
    private val stageRenderComponent: StageRenderComponent = StageRenderComponent(stage).also { this.add(it) }
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
        stageRenderComponent.stage.addActor(rootContainer)
    }

    private fun tab(): Container<Table> {
        val containerAbility = Container<Table>().align(Align.topRight)
        val table = Table()
        table.add(createButton("Physic", listOf(EventContext(PhysicSystem.DEBUG_ENABLE)))).width(50f).height(50f)
        table.row()
        table.add(createButton("UI", listOf(EventContext(UIService.DEBUG_ENABLE)))).width(50f).height(50f)
        table.row()
        table.add(createButton("Path", listOf(EventContext(UnitSystem.DEBUG_ENABLE)))).width(50f).height(50f)
        table.row()
        table.add(createButton("Map", listOf(EventContext(MapService.DEBUG_ENABLE)))).width(50f).height(50f)
        table.row()
        val list = listOf(
                EventContext(PlayerService.SPAWN, mapOf(Pair(PlayerService.PLAYER_NAME, "Player 1"))),
                EventContext(PlayerService.SPAWN, mapOf(Pair(PlayerService.PLAYER_NAME, "Player 2")))
        )
        table.add(createButton("Spawn", list)).width(50f).height(50f)
        containerAbility.actor = table
        return containerAbility
    }

    private fun createButton(text: String, events: List<EventContext>): TextButton {
        val button = TextButton(text, commonResources.skin)
        button.color = Color.DARK_GRAY
        button.color.a = 0.5f
        button.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                events.forEach { signal.dispatch(it) }
            }
        })
        return button
    }
}