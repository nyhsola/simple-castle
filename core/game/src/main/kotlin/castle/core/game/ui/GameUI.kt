package castle.core.game.ui

import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.common.`object`.CommonEntity
import castle.core.game.event.EventContext
import castle.core.game.event.EventQueue
import castle.core.game.event.EventType
import castle.core.game.service.GameResourceService
import castle.core.game.service.ScanService
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class GameUI(
    scanService: ScanService,
    private val resourceService: GameResourceService,
    guiConfig: GUIConfig
) : CommonEntity() {
    private val stageComponent: StageComponent = StageComponent(guiConfig.stage).also { this.add(it) }

    private val signal = Signal<EventContext>()

    private val rootContainer = Container<Table>()
    private val rootGroup = Table()

    val minimap: Minimap = Minimap(guiConfig, scanService)
    val chat = Chat(signal, resourceService)

    var debugEnabled: Boolean = false
        set(value) {
            stageComponent.stage.isDebugAll = value
            field = value
        }

    init {
        rootGroup.bottom()
        rootGroup.add(chatAndMapUI()).left()
        rootGroup.add(abilityBarUI()).expandX()

        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = rootGroup

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

    private fun chatAndMapUI(): Container<Table> {
        val tableChatMap = Table()
        val containerChatMap = Container<Table>()
        val chatUI = Container<Group>()
        val minimapUI = Container<Actor>()

        chatUI
            .height(Value.percentHeight(0.1f, rootGroup))
            .fillX()
        chatUI.actor = chat

        minimapUI
            .fill()
        minimapUI.actor = minimap

        tableChatMap
            .add(chatUI)
            .fillX()
        tableChatMap.row()
        tableChatMap
            .add(minimapUI)
            .grow()

        containerChatMap
            .left()
            .bottom()
            .width(Value.percentWidth(0.2f, rootContainer))
            .height(Value.percentHeight(0.3f, rootContainer))
        containerChatMap.actor = tableChatMap

        return containerChatMap
    }

    private fun abilityBarUI(): Container<Button> {
        val containerAbility = Container<Button>()
        val button = TextButton("Warrior", resourceService.skin)
        button.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                signal.dispatch(EventContext(EventType.UI1_BUTTON_CLICK))
            }
        })
        containerAbility.actor = button
        return containerAbility
    }
}