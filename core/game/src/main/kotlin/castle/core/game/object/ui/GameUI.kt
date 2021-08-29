package castle.core.game.`object`.ui

import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.game.GameContext
import castle.core.game.event.EventContext
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import castle.core.game.service.ScanService
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable

class GameUI(
    private val gameContext: GameContext,
    scanService: ScanService,
    guiConfig: GUIConfig,
    private val eventQueue: EventQueue
) : Disposable {
    private val entity = gameContext.engine.createEntity().apply { gameContext.engine.addEntity(this) }
    private val stageComponent: StageComponent = StageComponent(guiConfig.stage).apply { entity.add(this) }

    private val signal = Signal<EventContext>().apply { this.add(eventQueue) }

    private val rootContainer = Container<Group>()

    private val table = Table()
    private val tableContainer = Container<Table>()
    private val chatUI = Container<Group>()
    private val minimapUI = Container<Actor>()

    val minimap: Minimap = Minimap(guiConfig, scanService)
    val chat = Chat(signal, gameContext.resourceService)

    var debugEnabled: Boolean = false
        set(value) {
            stageComponent.stage.isDebugAll = value
            field = value
        }

    init {
        chatUI
            .height(Value.percentHeight(0.1f, tableContainer))
            .fillX()
        chatUI.actor = chat

        minimapUI
            .fill()
        minimapUI.actor = minimap

        table
            .add(chatUI)
            .fillX()
        table.row()
        table
            .add(minimapUI)
            .grow()

        tableContainer
            .left()
            .bottom()
            .width(Value.percentWidth(0.2f, rootContainer))
            .height(Value.percentHeight(0.3f, rootContainer))
        tableContainer.actor = table

        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = tableContainer

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

    override fun dispose() {
        gameContext.engine.removeEntity(entity)
    }
}