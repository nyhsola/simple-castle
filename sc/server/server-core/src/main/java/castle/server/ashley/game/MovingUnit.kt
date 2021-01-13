package castle.server.ashley.game

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.MessageManager
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

    var target: Matrix4? = null
    val stateMachine: StateMachine<MovingUnit, MovingUnitState> = DefaultStateMachine(this, MovingUnitState.STAND)

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
}