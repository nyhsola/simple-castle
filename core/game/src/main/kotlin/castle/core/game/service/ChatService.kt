package castle.core.game.service

import castle.core.game.ui.game.Chat
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import java.text.SimpleDateFormat
import java.util.*

class ChatService(
    private val chat: Chat
) {
    private val chatHistory: MutableList<String> = ArrayList()
    private val chatPoll: PriorityQueue<String> = PriorityQueue();

    init {
        chat.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    if (chat.typedText.isNotEmpty()) {
                        typeMessage(chat.typedText)
                        chat.typedText = ""
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
        val text = chatHistory
            .takeLast(chat.lines)
            .joinToString(separator = "\n") { it }
        chat.text = text
    }
}