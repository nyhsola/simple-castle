package castle.core.game.ui.game

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.common.event.EventContext
import castle.core.common.event.EventQueue
import castle.core.common.service.CommonResources
import castle.core.common.service.ScanService
import castle.core.common.system.CameraControlSystem
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align

class GameUI(
    scanService: ScanService,
    private val commonResources: CommonResources,
    guiConfig: GUIConfig,
    eventQueue: EventQueue
) : CommonEntity() {
    private val stageComponent: StageComponent = StageComponent(guiConfig.createStage()).also { this.add(it) }
    private val signal = Signal<EventContext>()
    private val rootContainer = Container<Table>()
    private val rootTable = Table()

    val minimap: Minimap = Minimap(guiConfig, scanService)
    val chat = Chat(signal, commonResources)

    var debugEnabled: Boolean = false
        set(value) {
            stageComponent.stage.isDebugAll = value
            field = value
        }

    init {
        signal.add(eventQueue)
        rootTable.bottom()
        rootTable.add(main()).grow()
        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = rootTable
        stageComponent.stage.addActor(rootContainer)
        stageComponent.stage.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (event.target is Group) {
                    signal.dispatch(EventContext(CameraControlSystem.CHAT_UNFOCUSED))
                    stageComponent.stage.unfocusAll()
                    stageComponent.stage.keyboardFocus = chat
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    private fun main(): Container<out Group> {
        val table = Table().align(Align.bottom)
        val container = Container<Table>().fill()
        table.add(chat)
            .height(Value.percentHeight(0.15f, container))
            .width(Value.percentWidth(0.25f, container))
            .align(Align.left)
        table.row()
        table.add(bottomBar()).growX()
        container.actor = table
        return container
    }

    private fun bottomBar(): Container<out Group> {
        val table = Table()
        val container = Container<Table>().fillX()
        table.add(minimap)
            .height(Value.percentHeight(1f, container))
            .width(Value.percentHeight(1f, container))
            .fill()
            .align(Align.left)
        table.add(rightTab()).expandX()
        container.actor = table
        return container
    }

    private fun rightTab(): Container<Table> {
        val containerAbility = Container<Table>()
        val table = Table()
        for (i in 0 until 4) {
            for (j in 0 until 5) {
                val button = TextButton("U$i$j", commonResources.skin)
                button.color = Color.DARK_GRAY
                table.add(button)
                    .width(50f)
                    .height(50f)
            }
            table.row()
        }
        containerAbility.actor = table
        return containerAbility
    }
}