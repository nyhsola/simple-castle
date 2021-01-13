package castle.server.ashley.game

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

enum class MovingUnitState : State<MovingUnit> {
    STAND {
        override fun enter(entity: MovingUnit) {
        }

        override fun update(entity: MovingUnit) {
        }

        override fun exit(entity: MovingUnit) {
        }

        override fun onMessage(entity: MovingUnit, telegram: Telegram): Boolean {
            val unitMessages = MovingUnit.Message.values()
            val idMessage = telegram.message
            if (idMessage in 0..unitMessages.size) {
                when (unitMessages[idMessage]) {
                    MovingUnit.Message.START_WALKING -> {
                        entity.stateMachine.changeState(WALK)
                        return true
                    }
                }
            }
            return false
        }
    },
    WALK {
        override fun enter(entity: MovingUnit) {
            entity.target = entity.paths.component2()
        }

        override fun update(entity: MovingUnit) {
            entity.updateMove()
        }

        override fun exit(entity: MovingUnit) {
        }

        override fun onMessage(entity: MovingUnit, telegram: Telegram): Boolean {
            return false
        }
    }
}