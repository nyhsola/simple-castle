package castle.server.ashley.systems.component

import castle.server.ashley.physic.PhysicInstance
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class PhysicComponent : Component, Disposable {
    lateinit var physicInstance: PhysicInstance

    override fun dispose() {
        physicInstance.dispose()
    }

    companion object {
        val mapper: ComponentMapper<PhysicComponent> = ComponentMapper.getFor(PhysicComponent::class.java)

        fun link(positionComponent: PositionComponent, physicComponent: PhysicComponent) {
            val physicInstance = physicComponent.physicInstance
            physicInstance.motionState.transform = positionComponent.matrix4
            physicInstance.body.motionState = physicInstance.motionState
            val mass = physicComponent.physicInstance.constructionInfo.mass
            if (mass != 0.0f) {
                physicInstance.body.collisionShape.calculateLocalInertia(mass, Vector3.Zero.cpy())
            }
        }
    }
}