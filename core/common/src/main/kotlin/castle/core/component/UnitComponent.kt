package castle.core.component

import castle.core.behaviour.controller.GroundUnitController
import castle.core.path.Area
import castle.core.util.Task
import castle.core.util.UnitUtils
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
    var distance: Float = GroundUnitController.Companion.Distances.AT.distance
    var deleteMe = false

    val path: MutableList<String> = ArrayList()
    var mainPath: GraphPath<Area> = DefaultGraphPath()
    var nextPath: Int = 0

    var addPath: GraphPath<Area> = DefaultGraphPath()
    var nextAddPath: Int = 0

    val targetMove: Vector2 = Vector2()
    var targetEnemy: UnitComponent? = null

    val inTouchObjects: MutableSet<Entity> = HashSet()

    val nextArea: Area
        get() = if (mainPath.count <= 0) Area(0,0) else mainPath[nextPath]
    val isDead: Boolean
        get() = currentHealth <= 0
    val isEnemiesInTouch: Boolean
        get() = inTouchEnemies.isNotEmpty()
    val inTouchEnemies: List<UnitComponent>
        get() = UnitUtils.findEnemies(this, UnitUtils.extractUnit(inTouchObjects))

    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            targetEnemy?.takeDamage(attackAmount.random())
        }
    }

    private fun takeDamage(amountParam: Int) {
        currentHealth = Integer.max(currentHealth - amountParam, 0)
    }
}