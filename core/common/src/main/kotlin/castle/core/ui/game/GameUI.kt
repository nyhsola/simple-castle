package castle.core.ui.game

import castle.core.`object`.CommonEntity
import castle.core.component.render.StageRenderComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.service.CommonResources
import castle.core.service.ScanService
import castle.core.system.GameManagerSystem
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align

class GameUI(
        scanService: ScanService,
        private val commonResources: CommonResources,
        stage: Stage,
        shapeRenderer: ShapeRenderer,
        eventQueue: EventQueue
) : CommonEntity() {
    private val stageRenderComponent: StageRenderComponent = StageRenderComponent(stage).also { this.add(it) }
    private val signal = Signal<EventContext>()
    private val rootContainer = Container<Table>()
    private val rootTable = Table()

    val minimap: Minimap = Minimap(shapeRenderer, scanService)
    val chat = Chat(signal, commonResources)
    val portrait = Portrait(commonResources)
    val description = Description(commonResources, scanService)

    var debugEnabled: Boolean = false
        set(value) {
            stageRenderComponent.stage.isDebugAll = value
            field = value
        }

    init {
        description.isDisabled = true
        description.style.font.data.markupEnabled = true
        description.touchable = Touchable.disabled
        description.isVisible = false

        signal.add(eventQueue)
        rootTable.bottom()
        rootTable.add(main()).grow()
        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = rootTable
        stageRenderComponent.stage.addActor(rootContainer)
        stageRenderComponent.stage.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (event.target is Group) {
                    signal.dispatch(EventContext(GameManagerSystem.CHAT_UNFOCUSED))
                    stageRenderComponent.stage.unfocusAll()
                    stageRenderComponent.stage.keyboardFocus = chat
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    fun update() {
        description.update()
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
                .expandX()
                .align(Align.left)
        table.add(description)
                .fill()
        table.add(portrait)
                .height(Value.percentHeight(1f, container))
                .width(Value.percentHeight(1f, container))
                .fill()
                .align(Align.right)
        table.add(buttonPanel())
        container.actor = table
        return container
    }

    private fun buttonPanel(): Container<Table> {
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