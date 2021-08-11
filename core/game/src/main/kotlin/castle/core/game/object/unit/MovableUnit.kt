package castle.core.game.`object`.unit

import castle.core.common.component.AnimationComponent
import castle.core.game.GameContext
import castle.core.game.`object`.DebugLine
import castle.core.game.`object`.GameMap
import castle.core.game.path.Area
import castle.core.game.utils.Constructor
import castle.core.game.utils.math.MathHelper
import castle.core.physic.service.PhysicListener
import castle.core.physic.service.PhysicService
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject

open class MovableUnit(
    constructor: Constructor,
    gameContext: GameContext,
    private val gameMap: GameMap
) : GameObject(constructor, gameContext) {
    companion object {
        const val BASE_SPEED: Float = 5f
    }
    private val stateMachine: StateMachine<MovableUnit, MovableUnitState> =
        DefaultStateMachine(this, MovableUnitState.STAND)
    private val tempVector2: Vector3 = Vector3()
    private val tempVector3: Vector3 = Vector3()
    private val tempVector4: Vector3 = Vector3()
    private val tempVector5: Vector3 = Vector3()
    private val tempVector6: Vector3 = Vector3()
    private val moveLine: DebugLine = DebugLine(gameContext)
    private var graphPath: GraphPath<Area> = DefaultGraphPath()
    private var graphPosition: Int = 0

    val nextPoint: Vector3
        get() {
            val graphPositionN = graphPosition + 1
            if (graphPath.count <= 0 || graphPositionN >= graphPath.count) {
                return Vector3.Zero
            }
            val position = graphPath.get(graphPositionN).position
            return Vector3(position.x, unitPosition.y, position.y)
        }

    open fun update() {
        stateMachine.update()
    }

    fun startRoute(pathsParam: List<Vector3>) {
        graphPosition = 0
        graphPath = gameMap.getPath(pathsParam)
        stateMachine.changeState(MovableUnitState.WALK)
    }

    private fun goNextPoint(it: Area) {
        if (gameMap.isInRangeOfArea(unitPosition, it)) {
            graphPosition++
            if (graphPosition >= graphPath.count) {
                graphPosition = graphPath.count - 1
            }
        }
    }

    private fun doOrIfNoPathStand(pathNum: Int, action: (Area) -> Unit) {
        if (pathNum >= 0 && pathNum < graphPath.count) {
            action.invoke(graphPath[pathNum])
        } else {
            stateMachine.changeState(MovableUnitState.STAND)
        }
    }

    private fun updateMove() {
        doOrIfNoPathStand(graphPosition) {
            val currentUnitPosition = unitPosition
            val targetFlat = it.position
            val target = tempVector2.set(targetFlat.x, currentUnitPosition.y, targetFlat.y)
            val direction = tempVector3.set(target).sub(currentUnitPosition).nor().scl(BASE_SPEED)
            val faceDirection = orientation.transform(tempVector4.set(defaultFaceDirection))

            if (MathHelper.getAngle(direction, faceDirection) !in 0.0..15.0) {
                val faceDirectionL = orientation.transform(tempVector5.set(defaultFaceDirectionL))
                val faceDirectionR = orientation.transform(tempVector6.set(defaultFaceDirectionR))
                val angleL = MathHelper.getAngle(direction, faceDirectionL)
                val angleR = MathHelper.getAngle(direction, faceDirectionR)
                angularVelocity = if (angleL < angleR) right else left
                linearVelocity = zero
            } else {
                linearVelocity = direction
                angularVelocity = zero
            }

            moveLine.show = true
            moveLine.from = currentUnitPosition
            moveLine.to = target
            moveLine.color = Color.GREEN

            goNextPoint(it)
        }
    }

    override fun dispose() {
        moveLine.dispose()
        super.dispose()
    }

    private enum class MovableUnitState : State<MovableUnit> {
        STAND {
            override fun enter(entity: MovableUnit) {
                entity.angularVelocity = zero
                entity.linearVelocity = zero
                entity.moveLine.show = false
            }

            override fun update(entity: MovableUnit) = Unit
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram) = false
        },
        WALK {
            override fun enter(entity: MovableUnit) {
                entity.moveLine.show = true
            }

            override fun update(entity: MovableUnit) = entity.updateMove()
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram) = false
        }
    }
}