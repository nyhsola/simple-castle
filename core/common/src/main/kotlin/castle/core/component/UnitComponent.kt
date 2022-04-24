package castle.core.component

import castle.core.behaviour.Behaviours
import castle.core.path.Area
import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2

class UnitComponent(
    owner: Entity,
    behaviour: String,
    val unitType: String,
    val amount: Int,
    val speedLinear: Float,
    val speedAngular: Float,
    val attackAmount: IntRange,
    val attackSpeed: Float,
    val scanRange: Float
) : Component {
    companion object {
        enum class Distances(val distance: Float) {
            AT(0.5f), MELEE(4f)
        }

        val mapper: ComponentMapper<UnitComponent> = ComponentMapper.getFor(UnitComponent::class.java)
    }

    var currentAmount = amount
    val state: StateMachine<Entity, State<Entity>> = DefaultStateMachine(owner, Behaviours.behaviors.getValue(behaviour))

    val target: Vector2 = Vector2()
    val path: MutableList<String> = ArrayList()
    var isMoving: Boolean = false
    var distance: Float = Distances.AT.distance
    var moveByPath: Boolean = false
    var moveByTarget: Boolean = false

    var graphPath: GraphPath<Area> = DefaultGraphPath()
    var toPosition: Int = 0
    val toArea: Area
        get() = graphPath[toPosition]

    var enableAttacking: Boolean = false
    var targetEnemy: Entity? = null
    var nearObjects: List<Entity> = ArrayList()
    var nearEnemies: List<Entity> = ArrayList()

    var playerName: String = "neutral"

    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            val moveComponent = mapper.get(targetEnemy)
            moveComponent.takeDamage(attackAmount.random())
        }
    }

    fun takeDamage(amountParam: Int) {
        currentAmount = Integer.max(currentAmount - amountParam, 0)
    }

    fun lockEnemy() {
        targetEnemy = nearEnemies.firstOrNull()
    }

    fun incrementPath() {
        toPosition = Integer.min(toPosition + 1, graphPath.count - 1)
    }

    fun goByPath() {
        moveByPath = true
        moveByTarget = false
    }

    fun goByTarget() {
        moveByPath = false
        moveByTarget = true
    }

    fun doNothing() {
        moveByPath = false
        moveByTarget = false
    }
}