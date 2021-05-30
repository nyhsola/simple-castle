package castle.server.ashley.game.unit

import castle.server.ashley.game.path.Area
import castle.server.ashley.math.MathHelper
import castle.server.ashley.service.GameContext
import castle.server.ashley.systems.component.AnimationComponent
import castle.server.ashley.systems.component.Line3DComponent
import castle.server.ashley.utils.Constructor
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class MovableUnit(
    constructor: Constructor,
    private val gameContext: GameContext,
    private val paths: List<Matrix4>
) : GameObject(constructor, gameContext.engine) {
    companion object {
        const val speedScalar: Float = 5f
        val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
        val defaultFaceDirectionL: Vector3 = Vector3(0f, 0f, -1f)
        val defaultFaceDirectionR: Vector3 = Vector3(0f, 0f, 1f)
    }

    private val right: Vector3 = Vector3(0f, 1f, 0f)
    private val left: Vector3 = Vector3(0f, -1f, 0f)
    private val zero: Vector3 = Vector3(0f, 0f, 0f)

    private val tempVector1: Vector3 = Vector3()
    private val tempVector2: Vector3 = Vector3()
    private val tempVector3: Vector3 = Vector3()
    private val tempVector4: Vector3 = Vector3()
    private val tempVector5: Vector3 = Vector3()
    private val tempVector6: Vector3 = Vector3()

    private val stateMachine: StateMachine<MovableUnit, MovableUnitState> = DefaultStateMachine(this, MovableUnitState.NOTHING)

    private val line3DComponent: Line3DComponent = gameContext.engine.createComponent(Line3DComponent::class.java).apply { entity.add(this) }
    private val animationComponent: AnimationComponent = gameContext.engine.createComponent(AnimationComponent::class.java).apply { entity.add(this) }

    private var positionInGraph: Int = 0

    private lateinit var graphPath: GraphPath<Area>

    init {
        animationComponent.animate = constructor.animation
    }

    fun update() {
        stateMachine.update()
    }

    fun updateMove() {
        updateMoveInternal()
    }

    private fun updateMoveInternal() {
        if (graphPath.count <= 0) {
            return
        }
        val currentUnitPosition = unitPosition
        updateNextPosition(currentUnitPosition)

        val areaFrom = graphPath.get(positionInGraph - 1)
        val areaTo = graphPath.get(positionInGraph)
        val targetFlat = gameContext.gameMap.toFlatPosition(areaTo)
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

        line3DComponent.from = currentUnitPosition
        line3DComponent.to = target
        line3DComponent.show = true

        gameContext.minimap.set(areaFrom.x, areaFrom.y)
    }

    private fun setInitialPosition() {
        unitPosition = paths[0].getTranslation(tempVector1)
    }

    private fun setInitialRotation() {
        if (graphPath.count <= 0) {
            return
        }
        val currentUnitPosition = unitPosition
        updateNextPosition(currentUnitPosition)

        val targetFlat = gameContext.gameMap.toFlatPosition(graphPath.get(positionInGraph))
        val target = tempVector2.set(targetFlat.x, currentUnitPosition.y, targetFlat.y)
        val direction = tempVector3.set(target).sub(currentUnitPosition).nor().scl(speedScalar)
        val faceDirection = orientation.transform(tempVector4.set(defaultFaceDirection))
        val angle = MathHelper.getAngle(direction, faceDirection)

        if (angle !in 0.0..5.0) {
            val faceDirectionL = orientation.transform(tempVector5.set(defaultFaceDirectionL))
            val faceDirectionR = orientation.transform(tempVector6.set(defaultFaceDirectionR))
            val angleL = MathHelper.getAngle(direction, faceDirectionL)
            val angleR = MathHelper.getAngle(direction, faceDirectionR)
            val worldTransformCpy = worldTransform
            if (angleL < angleR) {
                worldTransformCpy.rotate(Vector3.Y, angle.toFloat())
            } else {
                worldTransformCpy.rotate(Vector3.Y, -angle.toFloat())
            }
            worldTransform = worldTransformCpy
        }
    }

    private fun registerPath() {
        paths.component2().getTranslation(tempVector2)
        graphPath = gameContext.gameMap.getPath(unitPosition, tempVector2)
    }

    private fun updateNextPosition(unitPosition: Vector3) {
        if (positionInGraph + 1 >= graphPath.count) {
            return
        }
        if (gameContext.gameMap.isInRangeOfArea(unitPosition, graphPath[positionInGraph])) {
            positionInGraph++
        }
    }

    private enum class MovableUnitState : State<MovableUnit> {
        NOTHING {
            override fun enter(entity: MovableUnit) = Unit
            override fun update(entity: MovableUnit) = entity.stateMachine.changeState(STAND)
            override fun exit(entity: MovableUnit) = entity.setInitialPosition()
            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean = false
        },
        STAND {
            override fun enter(entity: MovableUnit) = entity.registerPath()
            override fun update(entity: MovableUnit) = entity.stateMachine.changeState(WALK)
            override fun exit(entity: MovableUnit) = entity.setInitialRotation()
            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean = false
        },
        WALK {
            override fun enter(entity: MovableUnit) = Unit
            override fun update(entity: MovableUnit) = entity.updateMove()
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean = false
        }
    }
}