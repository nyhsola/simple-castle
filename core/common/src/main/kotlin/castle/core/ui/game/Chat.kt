package castle.core.ui.game

import castle.core.service.CommonResources
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener
import java.text.SimpleDateFormat
import java.util.*

class Chat(commonResources: CommonResources) : Table() {
    private val chatHistory: MutableList<String> = ArrayList()
    private val chatPoll: PriorityQueue<String> = PriorityQueue()
    private val textArea = TextArea("", commonResources.skin)
    private val messageLine = Table()
    private val label = Label(": ", commonResources.skin)
    private val textField = TextField("", commonResources.skin)

    private val lines: Int
        get() {
            return textArea.linesShowing
        }

    var typedText: String
        get() {
            return textField.text
        }
        set(value) {
            textField.text = value
        }


    var text: String
        get() {
            return textArea.messageText
        }
        set(value) {
            textArea.messageText = value
        }


    init {
        touchable = Touchable.enabled

        textArea.color.a = 0.25f
        label.color.a = 0.65f
        textField.color.a = 0.65f

        add(textArea).grow()
        row()
        messageLine.add(label).fillY()
        messageLine.add(textField).growX()
        add(messageLine).growX()

        textArea.isDisabled = true
        textArea.style.font.data.markupEnabled = true
        textArea.touchable = Touchable.disabled

        addListener(object : FocusListener() {
            override fun keyboardFocusChanged(event: FocusEvent, actor: Actor, focused: Boolean) {
                if (focused) {
                    this@Chat.stage.keyboardFocus = textField
                }
            }
        })

        addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    if (typedText.isNotEmpty()) {
                        typeMessage(typedText)
                        typedText = ""
                    }
                }
                return false
            }
        })
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

    fun pollAllMessages(): Array<String> {
        val lastMessages = chatPoll.toTypedArray()
        chatPoll.clear()
        return lastMessages
    }

    private fun internalTypeMessage(message: String) {
        chatHistory.add(message)
        chatPoll.add(message)
        val resultText = chatHistory
            .takeLast(lines)
            .joinToString(separator = "\n") { it }
        text = resultText
    }
}