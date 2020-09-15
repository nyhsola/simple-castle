package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector3

class UnitComponent : Component {
    lateinit var aim: Vector3
    var previousAngle: Double = 0.0
    var rotateDirection: Boolean = false

    companion object {
        val mapper: ComponentMapper<UnitComponent> = ComponentMapper.getFor(UnitComponent::class.java)

        fun createComponent(engine: Engine, aim: Constructor): UnitComponent {
            val unitComponent: UnitComponent = engine.createComponent(UnitComponent::class.java)
            unitComponent.aim = aim.getTransform().getTranslation(Vector3())
            return unitComponent
        }

    }
}