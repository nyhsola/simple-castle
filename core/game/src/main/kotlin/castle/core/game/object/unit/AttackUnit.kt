package castle.core.game.`object`.unit

import castle.core.game.GameContext
import castle.core.common.json.Constructor
import castle.core.game.`object`.HPBar
import castle.core.game.service.MapService
import castle.core.game.util.Task
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.utils.Disposable
import java.lang.Float.min

class AttackUnit(
    constructor: Constructor,
    gameContext: GameContext,
    private val mapService: MapService
) : MovableUnit(constructor, gameContext, mapService), Disposable {
    private val stateMachine: StateMachine<AttackUnit, AttackUnitState> = DefaultStateMachine(this, AttackUnitState.PEACE)
    private var lastNearObjects: List<GameObject> = emptyList()
    private var enemy: AttackUnit? = null
    private val attackTask = object : Task(1f) {
        override fun action() {
            val random = damage.random()
            enemy?.takeDamage(random)
        }
    }
    private val hpBar: HPBar = HPBar(this, gameContext)
    private val healthPoints = 100.0f
    private var currHealthPoints = 100.0f
    private var damage = 50..75

    var isDead: Boolean = false

    private fun takeDamage(damage: Int) {
        currHealthPoints -= damage
        val percentage = min(currHealthPoints, 1.0f) / healthPoints
        hpBar.setPercentage(percentage)
        if (currHealthPoints <= 0) {
            isDead = true
        }
    }

    fun update(delta: Float) {
        if (!isDead) {
            attackTask.update(delta)
            stateMachine.update()
            super.update()
        }
    }

    override fun dispose() {
        hpBar.dispose()
        super.dispose()
    }

    private fun updatePeace() {
        lastNearObjects = mapService.getNearObjects(unitPosition)
        if (lastNearObjects.isNotEmpty()) {
            val nearEnemy = lastNearObjects[0]
            if (nearEnemy is AttackUnit) {
                startTrack(nearEnemy)
                enemy = nearEnemy
                stateMachine.changeState(AttackUnitState.PREPARE)
            }
        }
    }

    private fun updatePrepare() {
        if (isStand()) {
            stateMachine.changeState(AttackUnitState.ATTACK)
        }
    }

    private fun enterAttack() {
        attackTask.start()
    }

    private fun updateAttack() {
        if (enemy?.isDead == true) {
            attackTask.stop()
            continueRoute()
            stateMachine.changeState(AttackUnitState.PEACE)
        }
    }

    enum class AttackUnitState : State<AttackUnit> {
        PEACE {
            override fun enter(entity: AttackUnit) = Unit
            override fun update(entity: AttackUnit) = entity.updatePeace()
            override fun exit(entity: AttackUnit) = Unit
            override fun onMessage(entity: AttackUnit, telegram: Telegram) = false
        },
        PREPARE {
            override fun enter(entity: AttackUnit) = Unit
            override fun update(entity: AttackUnit) = entity.updatePrepare()
            override fun exit(entity: AttackUnit) = Unit
            override fun onMessage(entity: AttackUnit, telegram: Telegram) = false
        },
        ATTACK {
            override fun enter(entity: AttackUnit) {
                entity.enterAttack()
                entity.playAnimation("attack", 5f)
            }

            override fun update(entity: AttackUnit) = entity.updateAttack()
            override fun exit(entity: AttackUnit) = Unit
            override fun onMessage(entity: AttackUnit, telegram: Telegram) = false
        }
    }
}