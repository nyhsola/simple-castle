package castle.server.ashley.game.unit

import castle.server.ashley.systems.component.PhysicComponent
import castle.server.ashley.systems.component.PositionComponent
import castle.server.ashley.systems.component.RenderComponent
import castle.server.ashley.utils.Constructor
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

open class GameObject(private val engine: Engine) : Disposable {
    val entity: Entity = engine.createEntity()
    private val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java).apply { entity.add(this) }
    private val renderComponent: RenderComponent = engine.createComponent(RenderComponent::class.java).apply { entity.add(this) }
    private val physicComponent: PhysicComponent = engine.createComponent(PhysicComponent::class.java).apply { entity.add(this) }

    private val tempVector: Vector3 = Vector3()
    private val tempQuaternion: Quaternion = Quaternion()

    var unitPosition: Vector3
        get() = positionComponent.matrix4.getTranslation(tempVector)
        set(value) {
            positionComponent.matrix4.setTranslation(value)
        }

    var angularVelocity: Vector3
        get() = physicComponent.physicInstance.body.angularVelocity
        set(value) {
            physicComponent.physicInstance.body.angularVelocity = value
        }

    var linearVelocity: Vector3
        get() = physicComponent.physicInstance.body.linearVelocity
        set(value) {
            physicComponent.physicInstance.body.linearVelocity = value
        }

    var worldTransform: Matrix4
        get() = physicComponent.physicInstance.body.worldTransform
        set(value) {
            physicComponent.physicInstance.body.worldTransform = value
        }

    val orientation: Quaternion
        get() = worldTransform.getRotation(tempQuaternion)

    constructor(constructor: Constructor, engine: Engine) : this(engine) {
        positionComponent.matrix4 = constructor.getMatrix4()
        renderComponent.modelInstance = constructor.getModelInstance()
        physicComponent.physicInstance = constructor.getPhysicInstance()
        physicComponent.physicInstance.body.userData = constructor.node
    }

    override fun dispose() {
        engine.removeEntity(entity)
        physicComponent.physicInstance.dispose()
    }
}