package castle.server.ashley.game

import castle.server.ashley.component.StageComponent
import castle.server.ashley.creator.GUICreator
import castle.server.ashley.event.EventContext
import castle.server.ashley.event.EventType
import castle.server.ashley.utils.ResourceManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
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
import java.util.*
import kotlin.collections.ArrayList

class Chat(private val engine: Engine, guiCreator: GUICreator, signal: Signal<EventContext>, resourceManager: ResourceManager) : Disposable {
    private val entity = engine.createEntity()

    private val stageComponent: StageComponent = engine.createComponent(StageComponent::class.java)

    private val stage = guiCreator.createStage()
    private val table = Table()
    private val label = Label(": ", resourceManager.skin)
    private val textArea = TextArea("", resourceManager.skin)
    private val textField = TextField("", resourceManager.skin)

    private val chatHistory: MutableList<String> = ArrayList()
    private val chatPoll: PriorityQueue<String> = PriorityQueue();

    init {
        textArea.color.a = 0.7f
        label.color.a = 0.7f
        textField.color.a = 0.7f

        textArea.isDisabled = true
        textArea.setPrefRows(3f)

        table.add(textArea).fill().colspan(3)
        table.row()
        table.add(label).fill()
        table.add(textField).fill()

        table.align(Align.bottomLeft)

        stage.addActor(table)
        stageComponent.stage = stage
        engine.addEntity(entity.apply {
            add(stageComponent)
        })

        textField.addListener(object : FocusListener() {
            override fun keyboardFocusChanged(event: FocusEvent, actor: Actor, focused: Boolean) {
                if (focused) signal.dispatch(EventContext(EventType.CHAT_FOCUSED))
            }
        })

        textField.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER) {
                    stage.unfocus(textField)
                    chatHistory.add(textField.text)
                    chatPoll.add(textField.text)
                    textField.text = ""
                    textArea.text = chatHistory.takeLast(3).joinToString(separator = "\n") { "User: $it" }
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

    fun setDebug(enable: Boolean) {
        table.setDebug(enable, enable)
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