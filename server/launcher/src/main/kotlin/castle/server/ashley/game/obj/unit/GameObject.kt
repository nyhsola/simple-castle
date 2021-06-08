package castle.server.ashley.game.obj.unit

import castle.server.ashley.system.component.PhysicComponent
import castle.server.ashley.system.component.PositionComponent
import castle.server.ashley.system.component.RenderComponent
import castle.server.ashley.utils.Constructor
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

open class GameObject(
    constructor: Constructor,
    private val engine: Engine
) : Disposable {
    protected companion object {
        val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
        val defaultFaceDirectionL: Vector3 = Vector3(0f, 0f, -1f)
        val defaultFaceDirectionR: Vector3 = Vector3(0f, 0f, 1f)
        val right: Vector3 = Vector3(0f, 1f, 0f)
        val left: Vector3 = Vector3(0f, -1f, 0f)
        val zero: Vector3 = Vector3(0f, 0f, 0f)
    }

    val entity: Entity = engine.createEntity()
    private val positionComponent: PositionComponent = engine.createComponent(PositionComponent::class.java).apply { entity.add(this) }
    private val renderComponent: RenderComponent = engine.createComponent(RenderComponent::class.java).apply { entity.add(this) }
    private val physicComponent: PhysicComponent = engine.createComponent(PhysicComponent::class.java).apply { entity.add(this) }
    private val tempVector: Vector3 = Vector3()
    private val tempQuaternion: Quaternion = Quaternion()

    init {
        positionComponent.matrix4 = constructor.getMatrix4()
        renderComponent.modelInstance = constructor.getModelInstance()
        physicComponent.physicInstance = constructor.getPhysicInstance()
        physicComponent.physicInstance.body.userData = constructor.node
        engine.addEntity(entity)
    }

    val worldTransform: Matrix4
        get() = positionComponent.matrix4

    val orientation: Quaternion
        get() = worldTransform.getRotation(tempQuaternion)

    var unitPosition: Vector3
        get() = worldTransform.getTranslation(tempVector)
        set(value) {
            worldTransform.setTranslation(value)
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

    override fun dispose() {
        engine.removeEntity(entity)
        physicComponent.physicInstance.dispose()
    }
}