package castle.core.game.`object`.unit

import castle.core.game.GameContext
import castle.core.game.`object`.DebugLine
import castle.core.game.path.Area
import castle.core.common.json.Constructor
import castle.core.game.service.MapService
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

open class MovableUnit(
    constructor: Constructor,
    gameContext: GameContext,
    private val mapService: MapService
) : GameObject(constructor, gameContext) {
    companion object {
        private const val BASE_LINEAR_SPEED: Float = 5f
        private const val BASE_ANGULAR_SPEED: Float = 3f

        const val AT_POINT: Float = 0.3f
        const val MELEE: Float = 2f
    }
    private val stateMachine: StateMachine<MovableUnit, MovableUnitState> = DefaultStateMachine(this, MovableUnitState.STAND)
    private val tempTarget: Vector3 = Vector3()
    private val tempDirection: Vector3 = Vector3()
    private val tempQuaternion: Quaternion = Quaternion()
    private val moveLine: DebugLine = DebugLine(gameContext)
    private var graph: GraphPath<Area> = DefaultGraphPath()
    private var position: Int = 0
    private var trackObject: GameObject? = null

    private val nextPointTemp = Vector3()
    val nextPoint: Vector3
        get() {
            val nextPosition = position + 1
            if (graph.count <= 0 || nextPosition >= graph.count) {
                nextPointTemp.set(Vector3.Zero)
            } else {
                val position = graph.get(nextPosition).position
                nextPointTemp.set(position.x, unitPosition.y, position.y)
            }
            return nextPointTemp
        }

    open fun update() {
        stateMachine.update()
    }

    fun startRoute(pathsParam: List<Vector3>) {
        position = 0
        graph = mapService.getPath(pathsParam)
        stateMachine.changeState(MovableUnitState.ROUTE)
    }

    fun continueRoute() {
        stateMachine.changeState(MovableUnitState.ROUTE)
    }

    fun startTrack(trackObjectParam: GameObject) {
        trackObject = trackObjectParam
        stateMachine.changeState(MovableUnitState.TRACK)
    }

    fun stand() {
        stateMachine.changeState(MovableUnitState.STAND)
    }

    fun isStand(): Boolean {
        return stateMachine.isInState(MovableUnitState.STAND)
    }

    private fun updateRoute() {
        if (position < 0 || position >= graph.count) {
            stateMachine.changeState(MovableUnitState.STAND)
            return
        }
        val area = graph[position]
        val targetFlat = area.position
        val target = tempTarget.set(targetFlat.x, unitPosition.y, targetFlat.y)
        updateMove(target, AT_POINT)
        updatePosition(area)
    }

    private fun updateTrack() {
        val target = tempTarget.set(trackObject!!.unitPosition)
        updateMove(target, MELEE)
    }

    private fun updateMove(targetParam: Vector3, distance: Float) {
        updateLine(targetParam)
        val direction = tempDirection.set(targetParam).sub(unitPosition).nor()
        val angle = tempQuaternion.setFromCross(direction, faceDirection).angle
        val actualDistance = unitPosition.dst2(targetParam)
        val needRotation = angle !in 0.0..10.0
        when {
            needRotation -> {
                angularVelocity = (if (tempQuaternion.y < 0) right else left).cpy().scl(BASE_ANGULAR_SPEED)
                linearVelocity = zero
            }
            actualDistance > distance -> {
                linearVelocity = direction.scl(BASE_LINEAR_SPEED)
                angularVelocity = zero
            }
            else -> {
                stand()
            }
        }
    }

    private fun updatePosition(currentArea: Area) {
        val nextPosition = position + 1
        if (nextPosition < graph.count && mapService.withinArea(unitPosition, currentArea)) {
            position++
        }
    }

    private fun updateLine(target: Vector3) {
        moveLine.apply {
            show = true
            from = unitPosition
            to = target
            color = Color.GREEN
        }
    }

    override fun dispose() {
        moveLine.dispose()
        super.dispose()
    }

    private enum class MovableUnitState : State<MovableUnit> {
        STAND {
            override fun enter(entity: MovableUnit) {
                entity.playAnimation("stand", 1f)
                entity.angularVelocity = zero
                entity.linearVelocity = zero
                entity.moveLine.show = false
            }

            override fun update(entity: MovableUnit) = Unit
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram) = false
        },
        ROUTE {
            override fun enter(entity: MovableUnit) {
                entity.playAnimation("walk", 1f)
                entity.moveLine.show = true
            }

            override fun update(entity: MovableUnit) = entity.updateRoute()
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram) = false
        },
        TRACK {
            override fun enter(entity: MovableUnit) {
                entity.playAnimation("walk", 1f)
                entity.moveLine.show = true
            }

            override fun update(entity: MovableUnit) = entity.updateTrack()
            override fun exit(entity: MovableUnit) = Unit
            override fun onMessage(entity: MovableUnit, telegram: Telegram) = false
        }
    }
}