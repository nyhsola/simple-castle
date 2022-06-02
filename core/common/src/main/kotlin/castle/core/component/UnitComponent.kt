package castle.core.component

import castle.core.path.Area
import castle.core.physic.PhysicListener
import castle.core.service.UnitService
import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2

class UnitComponent(
    val owner: Entity,
    val totalHealth: Int,
    val attackAmount: IntRange,
    val attackSpeed: Float,
    val visibilityRange: Float,
    val speedLinear: Float,
    val speedAngular: Float
) : Component {
    companion object {
        val mapper: ComponentMapper<UnitComponent> = ComponentMapper.getFor(UnitComponent::class.java)
    }

    var playerName: String = "neutral"
    var needRotation: Boolean = false
    var needMove: Boolean = false
    var currentHealth = totalHealth
    var distance: Float = UnitService.Companion.Distances.AT.distance
    var deleteMe = false

    val path: MutableList<String> = ArrayList()
    var mainPath: GraphPath<Area> = DefaultGraphPath()
    var nextPath: Int = 0

    var addPath: GraphPath<Area> = DefaultGraphPath()
    var nextAddPath: Int = 0

    val targetMove: Vector2 = Vector2()
    var targetEnemy: UnitComponent? = null

    val nextArea: Area
        get() = if (mainPath.count <= 0) Area(0,0) else mainPath[nextPath]
    val isDead: Boolean
        get() = currentHealth <= 0
    val isEnemiesAround: Boolean
        get() = inRadiusEnemies.isNotEmpty()
    val isEnemiesInTouch: Boolean
        get() = inTouchEnemies.isNotEmpty()
    val inTouchEnemies: List<UnitComponent>
        get() = findEnemies(extractUnit(inTouchObjects))
    val inRadiusEnemies: List<UnitComponent>
        get() = findEnemies(inRadiusUnits)

    val inTouchObjects: MutableSet<Entity> = HashSet()
    val inRadiusUnits: MutableList<UnitComponent> = ArrayList()

    val physicListener = object : PhysicListener {
        override fun onContactStarted(entity1: Entity, entity2: Entity) {
            if (owner == entity1) {
                inTouchObjects.add(entity2)
            }
            if (owner == entity2) {
                inTouchObjects.add(entity1)
            }
        }

        override fun onContactEnded(entity1: Entity, entity2: Entity) {
            if (owner == entity1) {
                inTouchObjects.remove(entity2)
            }
            if (owner == entity2) {
                inTouchObjects.remove(entity1)
            }
        }
    }

    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            targetEnemy?.takeDamage(attackAmount.random())
        }
    }

    private fun takeDamage(amountParam: Int) {
        currentHealth = Integer.max(currentHealth - amountParam, 0)
    }

    private fun findEnemies(units: Collection<UnitComponent>): List<UnitComponent> {
        return units.filter { playerName != it.playerName }
    }

    private fun extractUnit(obj: Collection<Entity>): List<UnitComponent> {
        return obj.filter { mapper.has(it) }.map { mapper.get(it) }
    }
}