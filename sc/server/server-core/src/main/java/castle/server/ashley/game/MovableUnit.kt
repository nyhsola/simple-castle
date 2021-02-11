package castle.server.ashley.game

import castle.server.ashley.component.Line3DComponent
import castle.server.ashley.path.Area
import castle.server.ashley.physic.Constructor
import castle.server.ashley.service.GameContext
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants

class MovableUnit(constructor: Constructor, private val paths: List<Matrix4>, private val gameContext: GameContext) :
    GameObject(gameContext.engine, constructor) {

    enum class Message {
        START_WALKING
    }

    companion object {
        const val speedScalar: Float = 5f
    }

    private val tempVector1: Vector3 = Vector3()
    private val tempVector2: Vector3 = Vector3()

    private val line3DComponent: Line3DComponent = gameContext.engine.createComponent(Line3DComponent::class.java).apply { entity.add(this) }
    private var graphPath: GraphPath<Area>? = null
    private var positionInGraph: Int = 0

    val stateMachine: StateMachine<MovableUnit, MovableUnitState> = DefaultStateMachine(this, MovableUnitState.STAND)

    init {
        MessageManager.getInstance().dispatchMessage(1f, stateMachine, stateMachine, Message.START_WALKING.ordinal)

        val spawnPosition = paths.component1()
        positionComponent.matrix4.setTranslation(spawnPosition.getTranslation(tempVector1))
    }

    fun update(delta: Float) {
        stateMachine.update()
    }

    fun updateMove() {
        if (graphPath == null) return
        flatMoveToPositionInGraph()
    }

    fun registerPath() {
        positionComponent.matrix4.getTranslation(tempVector1)
        paths.component2().getTranslation(tempVector2)

        graphPath = gameContext.gameMap.getPath(tempVector1, tempVector2)
    }

    private fun flatMoveToPositionInGraph() {
        val graph = graphPath!!
        val unitPosition = positionComponent.matrix4.getTranslation(tempVector1)

        if (positionInGraph + 1 < graph.count && gameContext.gameMap.isInRangeOfArea(unitPosition, graph[positionInGraph])) {
            positionInGraph++
        }

        val targetFlat = gameContext.gameMap.toFlatPosition(graph.get(positionInGraph))
        val target = Vector3(targetFlat.x, unitPosition.y, targetFlat.y)

        val direction = tempVector2.set(target).sub(unitPosition).nor().scl(speedScalar)

        updateDebugLines(unitPosition, target)

        physicComponent.physicInstance.body.linearVelocity = direction
        physicComponent.physicInstance.body.activationState = CollisionConstants.ACTIVE_TAG
    }

    private fun updateDebugLines(from: Vector3, to: Vector3) {
        line3DComponent.from = from
        line3DComponent.to = to
        line3DComponent.show = true
    }

    enum class MovableUnitState : State<MovableUnit> {
        STAND {
            override fun enter(entity: MovableUnit) {
            }

            override fun update(entity: MovableUnit) {
            }

            override fun exit(entity: MovableUnit) {
            }

            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean {
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
            override fun enter(entity: MovableUnit) {
                entity.registerPath()
            }

            override fun update(entity: MovableUnit) {
                entity.updateMove()
            }

            override fun exit(entity: MovableUnit) {
            }

            override fun onMessage(entity: MovableUnit, telegram: Telegram): Boolean {
                return false
            }
        }
    }
}