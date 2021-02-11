package castle.server.ashley.game

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.physic.Constructor
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Disposable

open class GameObject(private val engine: Engine) : Disposable {
    val entity: Entity = engine.createEntity()
    private val renderComponent: RenderComponent = engine.createComponent(RenderComponent::class.java).apply { entity.add(this) }
    protected val physicComponent: PhysicComponent = engine.createComponent(PhysicComponent::class.java).apply { entity.add(this) }
    protected val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java).apply { entity.add(this) }

    constructor(engine: Engine, constructor: Constructor) : this(engine) {
        positionComponent.matrix4 = constructor.getMatrix4()
        renderComponent.modelInstance = constructor.getModelInstance()
        physicComponent.physicInstance = constructor.getPhysicInstance()
    }

    override fun dispose() {
        engine.removeEntity(entity)
        physicComponent.physicInstance.dispose()
    }
}