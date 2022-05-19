package castle.core.component

import castle.core.path.Area
import castle.core.physic.PhysicListener
import castle.core.service.UnitService
import castle.core.state.StateDelta
import castle.core.state.StateMachineDelta
import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class UnitComponent(
    behaviour: StateDelta<Entity>,
    val owner: Entity,
    val unitType: String,
    val amount: Int,
    val attackAmount: IntRange,
    val attackSpeed: Float,
    val visibilityRange: Float,
    val speedLinear: Float,
    val speedAngular: Float
) : Component {
    companion object {
        val mapper: ComponentMapper<UnitComponent> = ComponentMapper.getFor(UnitComponent::class.java)
    }

    private val temp: Vector3 = Vector3()
    val target: Vector2 = Vector2()
    var needRotation: Boolean = false
    var needMove: Boolean = false
    var distance: Float = UnitService.Companion.Distances.AT.distance
    var toPosition: Int = 0

    val toArea: Area
        get() = graphPath[toPosition]
    val position: Vector3
        get() = PositionComponent.mapper.get(owner).matrix4.getTranslation(temp)
    val isEnemiesAround: Boolean
        get() = nearEnemies.isNotEmpty()
    val isEnemiesInTouch: Boolean
        get() = inTouchEnemies.isNotEmpty()
    val isDead: Boolean
        get() = currentAmount <= 0

    val state: StateMachineDelta<Entity, StateDelta<Entity>> = StateMachineDelta(owner, behaviour)
    val path: MutableList<String> = ArrayList()

    val inTouchEnemies: MutableSet<UnitComponent> = HashSet()
    var nearUnits: List<UnitComponent> = ArrayList()
    var nearEnemies: List<UnitComponent> = ArrayList()
    var currentAmount = amount
    var isGoingMove: Boolean = false
    var graphPath: GraphPath<Area> = DefaultGraphPath()
    var targetEnemy: UnitComponent? = null

    var playerName: String = "neutral"

    val physicListener = object : PhysicListener {
        override fun onContactStarted(entity1: Entity, entity2: Entity) {
            if (!mapper.has(entity1) || !mapper.has(entity2)) return
            val unitComponent1 = mapper.get(entity1)
            val unitComponent2 = mapper.get(entity2)
            if (unitComponent2.playerName == unitComponent1.playerName) return
            if (owner == entity1) {
                inTouchEnemies.add(unitComponent2)
            }
            if (owner == entity2) {
                inTouchEnemies.add(unitComponent1)
            }
        }

        override fun onContactEnded(entity1: Entity, entity2: Entity) {
            if (!mapper.has(entity1) || !mapper.has(entity2)) return
            val unitComponent1 = mapper.get(entity1)
            val unitComponent2 = mapper.get(entity2)
            if (owner == entity1) {
                inTouchEnemies.remove(unitComponent2)
            }
            if (owner == entity2) {
                inTouchEnemies.remove(unitComponent1)
            }
        }
    }

    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            targetEnemy?.takeDamage(attackAmount.random())
        }
    }

    fun update(deltaTime: Float) {
        state.update(deltaTime)
    }

    private fun takeDamage(amountParam: Int) {
        currentAmount = Integer.max(currentAmount - amountParam, 0)
    }
}