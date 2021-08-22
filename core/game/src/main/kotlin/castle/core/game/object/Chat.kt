package castle.core.game.`object`

import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.game.event.EventContext
import castle.core.game.event.EventType
import castle.core.common.service.ResourceService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.*
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
    resourceService: ResourceService
) : Disposable {
    private val entity = engine.createEntity()

    private val stageComponent: StageComponent = engine.createComponent(StageComponent::class.java)

    private val stage = guiConfig.stage
    private val table = Table()
    private val label = Label(": ", resourceService.skin)
    private val textArea = TextArea("", resourceService.skin)
    private val textField = TextField("", resourceService.skin)

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

        table.add(textArea).fill().colspan(2).width(Gdx.graphics.width * 0.25f).height(Gdx.graphics.height * 0.25f)
        table.row()
        table.add(label).fill()
        table.add(textField).fill().expandX()

        table.align(Align.bottomLeft)

        stage.addActor(table)
        stageComponent.stage = stage

        textArea.isDisabled = true
        textArea.style.font.data.markupEnabled = true
        textArea.touchable = Touchable.disabled

        textField.addListener(object : FocusListener() {
            override fun keyboardFocusChanged(event: FocusEvent, actor: Actor, focused: Boolean) {
                if (focused) {
                    signal.dispatch(EventContext(EventType.CHAT_FOCUSED))
                }
            }
        })

        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    if (textField.text.isNotEmpty()) {
                        typeMessage(textField.text)
                        textField.text = ""
                    }
                }
                return super.keyDown(event, keycode)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (event.target is Group) {
                    stage.unfocus(textField)
                    signal.dispatch(EventContext(EventType.CHAT_UNFOCUSED))
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    stage.keyboardFocus = textField
                }
                return super.keyDown(event, keycode)
            }
        })
        engine.addEntity(entity.apply { add(stageComponent) })
    }

    fun typeMessage(message: String) {
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val color = (Math.random() * 0x1000000).toInt().toString(16)
        val fullMessage = "$currentTime: $message"
        fullMessage
            .split("\n")
            .flatMap { it.chunked(40) }
            .forEach { internalTypeMessage("[#$color]$it") }
    }

    private fun internalTypeMessage(message: String) {
        chatHistory.add(message)
        chatPoll.add(message)
        val text = chatHistory
            .takeLast(textArea.linesShowing)
            .joinToString(separator = "\n") { it }
        textArea.messageText = text
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