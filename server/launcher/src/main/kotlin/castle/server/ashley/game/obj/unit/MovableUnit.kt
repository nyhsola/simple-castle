package castle.server.ashley.game.obj.unit

import castle.server.ashley.game.GameContext
import castle.server.ashley.game.obj.DebugLine
import castle.server.ashley.game.path.Area
import castle.server.ashley.system.component.AnimationComponent
import castle.server.ashley.utils.Constructor
import castle.server.ashley.utils.math.MathHelper
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

open class MovableUnit(
    constructor: Constructor,
    private val gameContext: GameContext,
) : GameObject(constructor, gameContext.engine) {
    companion object {
        const val speedScalar: Float = 5f
    }

    private val stateMachine: StateMachine<MovableUnit, MovableUnitState> = DefaultStateMachine(this, MovableUnitState.STAND)
    private val tempVector2: Vector3 = Vector3()
    private val tempVector3: Vector3 = Vector3()
    private val tempVector4: Vector3 = Vector3()
    private val tempVector5: Vector3 = Vector3()
    private val tempVector6: Vector3 = Vector3()
    private val moveLine: DebugLine = DebugLine(gameContext)
    private val animationComponent: AnimationComponent = gameContext.engine.createComponent(AnimationComponent::class.java).apply { entity.add(this) }
    private var graphPath: GraphPath<Area> = DefaultGraphPath()
    private var positionInGraph: Int = 0
    private var nextPoint: Vector3 = Vector3()


    init {
        animationComponent.animate = constructor.animation
    }

    open fun update() {
        stateMachine.update()
    }

    fun walkTo(position: Vector3) {
        nextPoint.set(position)
        stateMachine.changeState(MovableUnitState.WALK)
    }

    override fun dispose() {
        moveLine.dispose()
        super.dispose()
    }

    private fun calculatePath() {
        positionInGraph = 0
        graphPath = gameContext.gameMap.getPath(unitPosition, nextPoint)
    }

    private fun updateMove() {
        if (graphPath.count <= 0) {
            return
        }
        val currentUnitPosition = unitPosition
        updateNextPosition(currentUnitPosition)

        val targetFlat = graphPath.get(positionInGraph).position
        val target = tempVector2.set(targetFlat.x, currentUnitPosition.y, targetFlat.y)
        val direction = tempVector3.set(target).sub(currentUnitPosition).nor().scl(speedScalar)
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
    }

    private fun setInitialRotation() {
        if (graphPath.count <= 0) {
            return
        }
        val currentUnitPosition = unitPosition
        updateNextPosition(currentUnitPosition)

        val targetFlat = graphPath.get(positionInGraph).position
        val target = tempVector2.set(targetFlat.x, currentUnitPosition.y, targetFlat.y)
        val direction = tempVector3.set(target).sub(currentUnitPosition).nor().scl(speedScalar)
        val faceDirection = orientation.transform(tempVector4.set(defaultFaceDirection))
        val angle = MathHelper.getAngle(direction, faceDirection)

        if (angle !in 0.0..5.0) {
            val faceDirectionL = orientation.transform(tempVector5.set(defaultFaceDirectionL))
            val faceDirectionR = orientation.transform(tempVector6.set(defaultFaceDirectionR))
            val angleL = MathHelper.getAngle(direction, faceDirectionL)
            val angleR = MathHelper.getAngle(direction, faceDirectionR)
            if (angleL < angleR) {
                worldTransform.rotate(Vector3.Y, angle.toFloat())
            } else {
                worldTransform.rotate(Vector3.Y, -angle.toFloat())
            }
        }
    }

    private fun updateNextPosition(unitPosition: Vector3) {
        if (positionInGraph + 1 >= graphPath.count) {
            return
        }
        if (gameContext.gameMap.isInRangeOfArea(unitPosition, graphPath[positionInGraph])) {
            positionInGraph++
        }
    }

    enum class MovableUnitState : State<MovableUnit> {
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
                entity.calculatePath()
                entity.setInitialRotation()
                entity.moveLine.show = true
            }

            override fun update(entity: MovableUnit) = entity.updateMove()
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram) = false
        }
    }
}