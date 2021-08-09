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
    private val paths: List<Pair<String, Matrix4>>,
    constructor: Constructor,
    gameContext: GameContext,
    private val gameMap: GameMap,
    private val physicService: PhysicService
) : GameObject(constructor, gameContext), PhysicListener {
    companion object {
        const val speedScalar: Float = 5f
    }

    private val stateMachine: StateMachine<MovableUnit, MovableUnitState> =
        DefaultStateMachine(this, MovableUnitState.STAND)
    private val tempVector1: Vector3 = Vector3()
    private val tempVector2: Vector3 = Vector3()
    private val tempVector3: Vector3 = Vector3()
    private val tempVector4: Vector3 = Vector3()
    private val tempVector5: Vector3 = Vector3()
    private val tempVector6: Vector3 = Vector3()
    private val moveLine: DebugLine = DebugLine(gameContext)
    private val animationComponent: AnimationComponent =
        gameContext.engine.createComponent(AnimationComponent::class.java).apply { entity.add(this) }
    private var graphPath: GraphPath<Area> = DefaultGraphPath()
    private var graphPosition: Int = 0
    private var areaPosition: Int = 0
    private var nextPoint: Vector3 = Vector3()

    init {
        animationComponent.animate = constructor.animation
        physicService.addListener(this)
    }

    override fun update() {
        stateMachine.update()
        super.update()
    }

    fun startWalking() {
        unitPosition = paths[areaPosition].second.getTranslation(tempVector1)
        areaPosition += 1
        walkTo(paths[areaPosition].second.getTranslation(tempVector1))
    }

    fun stand() {
        stateMachine.changeState(MovableUnitState.STAND)
    }

    private fun walkTo(position: Vector3) {
        nextPoint.set(position)
        stateMachine.changeState(MovableUnitState.WALK)
    }

    override fun dispose() {
        physicService.removeListener(this)
        moveLine.dispose()
        super.dispose()
    }

    private fun calculatePath() {
        graphPosition = 0
        graphPath = gameMap.getPath(unitPosition, nextPoint)
    }

    private fun updateMove() {
        if (graphPath.count <= 0) {
            return
        }
        val currentUnitPosition = unitPosition
        updateNextPosition(currentUnitPosition)

        val targetFlat = graphPath.get(graphPosition).position
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
        if (graphPosition !in 0..graphPath.count) {
            return
        }
        val currentUnitPosition = unitPosition
        updateNextPosition(currentUnitPosition)
        if (graphPosition <= 0 || graphPosition > graphPath.count) {
            return
        }
        val targetFlat = graphPath.get(graphPosition).position
        val target = tempVector2.set(targetFlat.x, unitPosition.y, targetFlat.y)
        val direction = tempVector3.set(target).sub(currentUnitPosition)
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
        if (graphPosition + 1 >= graphPath.count) {
            return
        }
        if (gameMap.isInRangeOfArea(unitPosition, graphPath[graphPosition])) {
            graphPosition++
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

    override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {
        val userData1 = colObj0.userData as String
        val userData2 = colObj1.userData as String

        if (userData2 == uuid && userData1.startsWith(paths[areaPosition].first)) {
            startWalking()
        }
    }
}