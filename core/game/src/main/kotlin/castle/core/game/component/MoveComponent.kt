package castle.core.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector2

class MoveComponent(
    val speedLinear: Float,
    val speedAngular: Float,
    val target: Vector2 = Vector2(),
    var enableMoving: Boolean = false,
    var isMoving: Boolean = false,
    var distance: Float = Distances.AT.distance
) : Component {
    companion object {
        enum class Distances(val distance: Float) {
            AT(0.3f), MELEE(4f)
        }

        val mapper: ComponentMapper<MoveComponent> = ComponentMapper.getFor(MoveComponent::class.java)
    }
}