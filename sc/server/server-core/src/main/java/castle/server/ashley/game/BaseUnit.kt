package castle.server.ashley.game

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Engine

open class BaseUnit(engine: Engine) {
    private val entity = engine.createEntity()
    private val renderComponent: RenderComponent = engine.createComponent(RenderComponent::class.java)
    val physicComponent: PhysicComponent = engine.createComponent(PhysicComponent::class.java)
    val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java)

    init {
        engine.addEntity(entity.apply {
            add(positionComponent)
            add(renderComponent)
            add(physicComponent)
        })
    }

    constructor(engine: Engine, constructor: Constructor) : this(engine) {
        positionComponent.matrix4 = constructor.getMatrix4()
        renderComponent.modelInstance = constructor.getModelInstance()
        physicComponent.physicInstance = constructor.getPhysicInstance()
    }
}