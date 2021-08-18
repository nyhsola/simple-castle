package castle.core.game.`object`.unit

import castle.core.game.GameContext
import castle.core.game.`object`.GameMap
import castle.core.common.json.Constructor
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram

class AttackUnit(
    constructor: Constructor,
    gameContext: GameContext,
    private val gameMap: GameMap
) : MovableUnit(constructor, gameContext, gameMap) {
    private val stateMachine: StateMachine<AttackUnit, AttackUnitState> = DefaultStateMachine(this, AttackUnitState.PREPARE)
    private var lastNearObjects: List<GameObject> = emptyList()
    private var enemy: GameObject? = null

    override fun update() {
        stateMachine.update()
        super.update()
    }

    private fun enterAttack() {
        startTrack(enemy!!)
    }

    private fun updateAttack() {
        if (isStand()) {
            playAnimation("attack", 5f)
        }
    }

    private fun updatePeace() {
        lastNearObjects = gameMap.getNearObjects(unitPosition)
        if (lastNearObjects.isNotEmpty()) {
            enemy = lastNearObjects[0]
            stateMachine.changeState(AttackUnitState.ATTACK)
        }
    }

    enum class AttackUnitState : State<AttackUnit> {
        PREPARE {
            override fun enter(entity: AttackUnit) = Unit
            override fun update(entity: AttackUnit) = entity.updatePeace()
            override fun exit(entity: AttackUnit) = Unit
            override fun onMessage(entity: AttackUnit, telegram: Telegram) = false
        },
        ATTACK {
            override fun enter(entity: AttackUnit) = entity.enterAttack()
            override fun update(entity: AttackUnit) = entity.updateAttack()
            override fun exit(entity: AttackUnit) = Unit
            override fun onMessage(entity: AttackUnit, telegram: Telegram) = false
        }
    }
}