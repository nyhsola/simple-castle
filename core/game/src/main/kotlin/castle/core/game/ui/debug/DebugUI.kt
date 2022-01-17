package castle.core.game.ui.debug

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.game.event.EventContext
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import castle.core.game.service.GameResourceService
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align

class DebugUI(
    private val resourceService: GameResourceService,
    guiConfig: GUIConfig
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
        rootContainer.isVisible = false

        rootTable.add(tab()).grow()
        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = rootTable
        stageComponent.stage.addActor(rootContainer)
    }

    fun addListener(eventQueue: EventQueue) {
        signal.add(eventQueue)
    }

    private fun tab(): Container<Table> {
        val containerAbility = Container<Table>().align(Align.topRight)
        val table = Table()
        table.add(createButton("Physic", EventType.PHYSIC_ENABLE)).width(50f).height(50f)
        table.add(createButton("UI", EventType.UI_ENABLE)).width(50f).height(50f)
        containerAbility.actor = table
        return containerAbility
    }

    private fun createButton(text: String, eventType: EventType): TextButton {
        val button = TextButton(text, resourceService.skin)
        button.color = Color.DARK_GRAY
        button.color.a = 0.5f
        button.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                signal.dispatch(EventContext(eventType))
            }
        })
        return button
    }
}