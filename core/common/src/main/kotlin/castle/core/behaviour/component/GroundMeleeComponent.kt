package castle.core.behaviour.component

import castle.core.behaviour.controller.GroundMeleeUnitController
import castle.core.component.UnitComponent
import castle.core.path.Area
import castle.core.util.Task
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.math.Vector2

class GroundMeleeComponent(
    val attackAmount: IntRange,
    val attackSpeed: Float,
    val speedLinear: Float,
    val speedAngular: Float
) : Component {
    companion object {
        val mapper: ComponentMapper<GroundMeleeComponent> = ComponentMapper.getFor(GroundMeleeComponent::class.java)
    }

    var needRotation: Boolean = false
    var needMove: Boolean = false
    var distance: Float = GroundMeleeUnitController.Distances.AT.distance
    val path: MutableList<String> = ArrayList()
    var mainPath: GraphPath<Area> = DefaultGraphPath()
    var nextPath: Int = 0
    val targetMove: Vector2 = Vector2()
    var targetEnemy: UnitComponent? = null

    val nextArea: Area
        get() = if (mainPath.count <= 0 || nextPath > mainPath.count - 1) Area(0, 0) else mainPath[nextPath]

    val attackTask: Task = object : Task(attackSpeed) {
        override fun action() {
            targetEnemy?.takeDamage(attackAmount.random())
        }
    }
}