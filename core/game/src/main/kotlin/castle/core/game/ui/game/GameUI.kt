package castle.core.game.ui.game

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.game.event.EventContext
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import castle.core.game.service.GameResourceService
import castle.core.game.service.ScanService
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align

class GameUI(
    scanService: ScanService,
    private val resourceService: GameResourceService,
    guiConfig: GUIConfig
) : CommonEntity() {
    private val stageComponent: StageComponent = StageComponent(guiConfig.createStage()).also { this.add(it) }
    private val signal = Signal<EventContext>()
    private val rootContainer = Container<Table>()
    private val rootTable = Table()

    val minimap: Minimap = Minimap(guiConfig, scanService)
    val chat = Chat(signal, resourceService)

    var debugEnabled: Boolean = false
        set(value) {
            stageComponent.stage.isDebugAll = value
            field = value
        }

    init {
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
                    signal.dispatch(EventContext(EventType.CHAT_UNFOCUSED))
                    stageComponent.stage.unfocusAll()
                    stageComponent.stage.keyboardFocus = chat
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    fun addListener(eventQueue: EventQueue) {
        signal.add(eventQueue)
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
                val button = TextButton("U$i$j", resourceService.skin)
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