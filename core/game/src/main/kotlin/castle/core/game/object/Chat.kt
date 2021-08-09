package castle.core.game.`object`

import castle.core.common.component.StageComponent
import castle.core.common.creator.GUIConfig
import castle.core.game.event.EventContext
import castle.core.game.event.EventType
import castle.core.game.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import java.text.SimpleDateFormat
import java.util.*


class Chat(
    private val engine: Engine,
    guiConfig: GUIConfig,
    signal: Signal<EventContext>,
    resourceManager: ResourceManager
) : Disposable {
    private val entity = engine.createEntity()

    private val stageComponent: StageComponent = engine.createComponent(StageComponent::class.java)

    private val stage = guiConfig.stage()
    private val table = Table()
    private val label = Label(": ", resourceManager.skin)
    private val textArea = TextArea("", resourceManager.skin)
    private val textField = TextField("", resourceManager.skin)

    private val chatHistory: MutableList<String> = ArrayList()
    private val chatPoll: PriorityQueue<String> = PriorityQueue();

    var debugEnabled: Boolean = false
        set(value) {
            table.setDebug(value, value)
            field = value
        }

    init {
        textArea.color.a = 0.7f
        label.color.a = 0.7f
        textField.color.a = 0.7f

        textArea.isDisabled = true

        table.add(textArea).fill().colspan(2).width(Gdx.graphics.width * 0.25f).height(Gdx.graphics.height * 0.25f)
        table.row()
        table.add(label).fill()
        table.add(textField).fill().expandX()

        table.align(Align.bottomLeft)

        stage.addActor(table)
        stageComponent.stage = stage
        engine.addEntity(entity.apply {
            add(stageComponent)
        })

        textField.addListener(object : FocusListener() {
            override fun keyboardFocusChanged(event: FocusEvent, actor: Actor, focused: Boolean) {
                if (focused) {
                    signal.dispatch(EventContext(EventType.CHAT_FOCUSED))
                }
            }
        })

        textField.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER) {
                    typeMessage(textField.text)
                    textField.text = ""
                    stage.unfocus(textField)
                }
                if (keycode == Input.Keys.TAB) {
                    stage.unfocus(textField)
                    signal.dispatch(EventContext(EventType.CHAT_UNFOCUSED))
                }
                return super.keyDown(event, keycode)
            }
        })

        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER) {
                    stage.keyboardFocus = textField
                }
                return super.keyDown(event, keycode)
            }
        })
    }

    fun typeMessage(message: String) {
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val messageEd = "$currentTime: $message"
        chatHistory.add(messageEd)
        chatPoll.add(messageEd)
        textArea.text = chatHistory.takeLast(textArea.linesShowing).joinToString(separator = "\n") { it }
    }

    fun pollAllMessages(): Array<String> {
        val lastMessages = chatPoll.toTypedArray()
        chatPoll.clear()
        return lastMessages
    }

    override fun dispose() {
        engine.removeEntity(entity)
    }
}