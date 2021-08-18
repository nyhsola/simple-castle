package castle.core.common.component

import castle.core.common.json.Constructor
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.ModelInstance
import java.util.*

class RenderComponent(positionComponent: PositionComponent, constructor: Constructor) : Component {
    val modelInstance: ModelInstance = constructor.getModelInstance()
    var hide: Boolean = constructor.hide

    init {
        modelInstance.transform = positionComponent.matrix4
    }

    companion object {
        val mapper: ComponentMapper<RenderComponent> = ComponentMapper.getFor(RenderComponent::class.java)
    }
}