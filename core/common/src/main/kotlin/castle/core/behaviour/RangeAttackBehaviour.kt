package castle.core.behaviour

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

enum class RangeAttackBehaviour : State<Entity> {
    INIT {
        override fun enter(entity: Entity) {}

        override fun update(entity: Entity) {}

        override fun exit(entity: Entity) {}

        override fun onMessage(entity: Entity, telegram: Telegram) = false
    }
}