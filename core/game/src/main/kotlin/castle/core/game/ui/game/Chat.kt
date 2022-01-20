package castle.core.game.ui.game

import castle.core.common.event.EventContext
import castle.core.common.service.CommonResources
import castle.core.common.system.CameraControlSystem
import com.badlogic.ashley.signals.Signal
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

class Chat(
    signal: Signal<EventContext>,
    commonResources: CommonResources
) : Table() {
    private val textArea = TextArea("", commonResources.skin)
    private val messageLine = Table()
    private val label = Label(": ", commonResources.skin)
    private val textField = TextField("", commonResources.skin)

    val lines: Int
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
                    signal.dispatch(EventContext(CameraControlSystem.CHAT_FOCUSED))
                }
            }
        })

        addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    this@Chat.stage.keyboardFocus = textField
                }
                return false
            }
        })
    }
}