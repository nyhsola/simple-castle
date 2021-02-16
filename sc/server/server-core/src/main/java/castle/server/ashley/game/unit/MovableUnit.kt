package castle.server.ashley.game.unit

import castle.server.ashley.game.path.Area
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
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants
import kotlin.math.acos

class MovableUnit(constructor: Constructor, private val paths: List<Matrix4>, private val gameContext: GameContext) :
    GameObject(gameContext.engine, constructor) {
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

    val stateMachine: StateMachine<MovableUnit, MovableUnitState> = DefaultStateMachine(this, MovableUnitState.NOTHING)

    private val line3DComponent: Line3DComponent = gameContext.engine.createComponent(Line3DComponent::class.java).apply { entity.add(this) }
    private val animationComponent: AnimationComponent = gameContext.engine.createComponent(AnimationComponent::class.java).apply { entity.add(this) }

    private lateinit var graphPath: GraphPath<Area>
    private var positionInGraph: Int = 0

    init {
        positionComponent.matrix4.setTranslation(paths[0].getTranslation(tempVector1))
        animationComponent.animate = constructor.animation
    }

    fun update(delta: Float) {
        stateMachine.update()
    }

    fun updateMove() {
        val unitPosition = positionComponent.matrix4.getTranslation(tempVector1)

        if (positionInGraph + 1 < graphPath.count && gameContext.gameMap.isInRangeOfArea(unitPosition, graphPath[positionInGraph])) {
            positionInGraph++
        }

        val targetFlat = gameContext.gameMap.toFlatPosition(graphPath.get(positionInGraph))
        val target = tempVector2.set(targetFlat.x, unitPosition.y, targetFlat.y)
        val direction = tempVector3.set(target).sub(unitPosition).nor().scl(speedScalar)
        val faceDirection = physicComponent.physicInstance.body.orientation.transform(tempVector4.set(defaultFaceDirection))

        if (getAngle(direction, faceDirection) !in 0.0..15.0) {
            val faceDirectionL = physicComponent.physicInstance.body.orientation.transform(tempVector5.set(defaultFaceDirectionL))
            val faceDirectionR = physicComponent.physicInstance.body.orientation.transform(tempVector6.set(defaultFaceDirectionR))
            if (getAngle(direction, faceDirectionL) < getAngle(direction, faceDirectionR)) {
                physicComponent.physicInstance.body.angularVelocity = right
            } else {
                physicComponent.physicInstance.body.angularVelocity = left
            }
            physicComponent.physicInstance.body.linearVelocity = zero
        } else {
            physicComponent.physicInstance.body.linearVelocity = direction
            physicComponent.physicInstance.body.angularVelocity = zero
        }

        line3DComponent.from = unitPosition
        line3DComponent.to = target
        line3DComponent.show = true

        physicComponent.physicInstance.body.activationState = CollisionConstants.ACTIVE_TAG
    }

    fun setInitialRotation() {
        val unitPosition = positionComponent.matrix4.getTranslation(tempVector1)

        if (positionInGraph + 1 < graphPath.count && gameContext.gameMap.isInRangeOfArea(unitPosition, graphPath[positionInGraph])) {
            positionInGraph++
        }

        val targetFlat = gameContext.gameMap.toFlatPosition(graphPath.get(positionInGraph))
        val target = tempVector2.set(targetFlat.x, unitPosition.y, targetFlat.y)
        val direction = tempVector3.set(target).sub(unitPosition).nor().scl(speedScalar)
        val faceDirection = physicComponent.physicInstance.body.orientation.transform(tempVector4.set(defaultFaceDirection))

        val angle = getAngle(direction, faceDirection)
        if (angle !in 0.0..5.0) {
            val faceDirectionL = physicComponent.physicInstance.body.orientation.transform(tempVector5.set(defaultFaceDirectionL))
            val faceDirectionR = physicComponent.physicInstance.body.orientation.transform(tempVector6.set(defaultFaceDirectionR))

            val worldTransform = physicComponent.physicInstance.body.worldTransform

            val angleL = getAngle(direction, faceDirectionL)
            val angleR = getAngle(direction, faceDirectionR)

            if (angleL < angleR) {
                worldTransform.rotate(Vector3.Y, angle.toFloat())
            } else {
                worldTransform.rotate(Vector3.Y, -angle.toFloat())
            }

            physicComponent.physicInstance.body.worldTransform = worldTransform
        }
    }

    fun registerPath() {
        positionComponent.matrix4.getTranslation(tempVector1)
        paths.component2().getTranslation(tempVector2)
        graphPath = gameContext.gameMap.getPath(tempVector1, tempVector2)
    }

    private fun getAngle(a: Vector3, b: Vector3): Double {
        val aNor = a.cpy().nor()
        val bNor = b.cpy().nor()
        return Math.toDegrees(acos(aNor.dot(bNor).toDouble()))
    }

    enum class MovableUnitState : State<MovableUnit> {
        NOTHING {
            override fun enter(entity: MovableUnit) = Unit
            override fun update(entity: MovableUnit) = entity.stateMachine.changeState(STAND)
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean = false
        },
        STAND {
            override fun enter(entity: MovableUnit) = entity.registerPath()
            override fun update(entity: MovableUnit) = entity.stateMachine.changeState(WALK)
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean = false
        },
        WALK {
            override fun enter(entity: MovableUnit) = entity.setInitialRotation()
            override fun update(entity: MovableUnit) = entity.updateMove()
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean = false
        }
    }
}