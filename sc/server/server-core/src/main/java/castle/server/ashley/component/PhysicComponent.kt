package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import castle.server.ashley.physic.PhysicObject
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class PhysicComponent : Component, Disposable {
    lateinit var physicObject: PhysicObject
    var mass: Float = 0.0f

    override fun dispose() {
        physicObject.dispose()
    }

    companion object {
        val mapper: ComponentMapper<PhysicComponent> = ComponentMapper.getFor(PhysicComponent::class.java)

        fun createComponent(engine: Engine, constructor: Constructor): PhysicComponent {
            val physicComponent: PhysicComponent = engine.createComponent(PhysicComponent::class.java)
            physicComponent.physicObject = constructor.getPhysicObject()
            physicComponent.mass = constructor.mass
            return physicComponent
        }

        fun link(positionComponent: PositionComponent, physicComponent: PhysicComponent) {
            val physicObject = physicComponent.physicObject

            physicObject.motionState.transform = positionComponent.matrix4
            physicObject.body.motionState = physicObject.motionState
            physicObject.body.userData = positionComponent.nodeName

            if (physicComponent.mass != 0.0f) {
                physicObject.body.collisionShape.calculateLocalInertia(physicComponent.mass, Vector3.Zero.cpy())
            }
        }
    }

    fun setAngularFactor(angularFactor: Vector3) {
        physicObject.body.angularFactor.set(angularFactor)
    }
}