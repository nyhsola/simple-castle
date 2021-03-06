package castle.server.ashley.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.ModelInstance

class RenderComponent : Component {
    lateinit var modelInstance: ModelInstance

    companion object {
        val mapper: ComponentMapper<RenderComponent> = ComponentMapper.getFor(RenderComponent::class.java)

        fun postConstruct(positionComponent: PositionComponent, renderComponent: RenderComponent) {
            renderComponent.modelInstance.transform = positionComponent.matrix4
        }
    }
}