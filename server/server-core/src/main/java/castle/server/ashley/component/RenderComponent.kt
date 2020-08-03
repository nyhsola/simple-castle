package castle.server.ashley.component

import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g3d.ModelInstance

class RenderComponent : Component {
    lateinit var modelInstance: ModelInstance
    var hide: Boolean = false

    companion object {
        val mapper: ComponentMapper<RenderComponent> = ComponentMapper.getFor(RenderComponent::class.java)

        fun createComponent(engine: Engine, constructor: Constructor): RenderComponent {
            val renderComponent: RenderComponent = engine.createComponent(RenderComponent::class.java)
            renderComponent.modelInstance = constructor.getModel()
            renderComponent.hide = constructor.hide
            return renderComponent
        }

        fun link(positionComponent: PositionComponent, renderComponent: RenderComponent) {
            renderComponent.modelInstance.transform = positionComponent.matrix4
        }
    }
}