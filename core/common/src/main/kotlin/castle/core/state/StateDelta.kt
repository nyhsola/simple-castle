package castle.core.state

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

interface StateDelta<E> : State<E> {
    fun update(entity: E, delta: Float) {}

    override fun update(entity: E) {
        throw java.lang.IllegalStateException("Use delta method!")
    }

    override fun enter(entity: E) {}

    override fun exit(entity: E) {}

    override fun onMessage(entity: E, telegram: Telegram): Boolean = false
}