package castle.server.ashley.game

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants

class MovingUnit(engine: Engine, constructor: Constructor, val paths: List<Matrix4>) : BaseUnit(engine, constructor) {
    enum class Message {
        START_WALKING
    }

    companion object {
        const val speedScalar: Float = 10f
    }

    private val tempVector1: Vector3 = Vector3()
    private val tempVector2: Vector3 = Vector3()

    val stateMachine: StateMachine<MovingUnit, MovingUnitState> = DefaultStateMachine(this, MovingUnitState.STAND)
    var target: Matrix4? = null

    init {
        MessageManager.getInstance().dispatchMessage(1f, stateMachine, stateMachine, Message.START_WALKING.ordinal)
        val spawnPosition = paths.component1()
        positionComponent.matrix4.setTranslation(spawnPosition.getTranslation(tempVector1))
    }

    fun update(delta: Float) {
        stateMachine.update()
    }

    fun updateMove() {
        val unitPosition = positionComponent.matrix4.getTranslation(tempVector1)
        val targetPosition = target!!.getTranslation(tempVector2)
        val direction = targetPosition.sub(unitPosition).nor().scl(speedScalar).cpy()
        physicComponent.physicInstance.body.linearVelocity = direction
        physicComponent.physicInstance.body.activationState = CollisionConstants.ACTIVE_TAG
    }

    enum class MovingUnitState : State<MovingUnit> {
        STAND {
            override fun enter(entity: MovingUnit) {
            }

            override fun update(entity: MovingUnit) {
            }

            override fun exit(entity: MovingUnit) {
            }

            override fun onMessage(entity: MovingUnit, telegram: Telegram): Boolean {
                val unitMessages = Message.values()
                val idMessage = telegram.message
                if (idMessage in 0..unitMessages.size) {
                    when (unitMessages[idMessage]) {
                        Message.START_WALKING -> {
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
}