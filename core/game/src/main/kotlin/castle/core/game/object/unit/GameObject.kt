package castle.core.game.`object`.unit

import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.game.GameContext
import castle.core.game.utils.Constructor
import castle.core.game.utils.math.MathHelper
import castle.core.physic.component.PhysicComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import java.util.*
import kotlin.math.acos

open class GameObject(
    constructor: Constructor,
    private val gameContext: GameContext,
) : Disposable {
    protected companion object {
        val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
        val defaultFaceDirectionL: Vector3 = Vector3(0f, 0f, -1f)
        val defaultFaceDirectionR: Vector3 = Vector3(0f, 0f, 1f)
        val right: Vector3 = Vector3(0f, 1f, 0f)
        val left: Vector3 = Vector3(0f, -1f, 0f)
        val zero: Vector3 = Vector3(0f, 0f, 0f)
    }

    val entity: Entity = gameContext.engine.createEntity()
    private val positionComponent: PositionComponent =
        gameContext.engine.createComponent(PositionComponent::class.java).apply { entity.add(this) }
    private val renderComponent: RenderComponent =
        gameContext.engine.createComponent(RenderComponent::class.java).apply { entity.add(this) }
    private val physicComponent: PhysicComponent =
        gameContext.engine.createComponent(PhysicComponent::class.java).apply { entity.add(this) }
    private val tempVector1: Vector3 = Vector3()
    private val tempVector2: Vector3 = Vector3()
    private val tempVector3: Vector3 = Vector3()
    private val tempVector4: Vector3 = Vector3()
    private val tempQuaternion: Quaternion = Quaternion()

    init {
        positionComponent.matrix4 = constructor.getMatrix4()
        renderComponent.modelInstance = constructor.getModelInstance()
        renderComponent.hide = constructor.hide
        physicComponent.physicInstance = constructor.getPhysicInstance()
        physicComponent.physicInstance.body.userData = constructor.node + "-" + UUID.randomUUID().toString()
        gameContext.engine.addEntity(entity)
    }

    val uuid: String
        get() = physicComponent.physicInstance.body.userData as String

    private val worldTransform: Matrix4
        get() = positionComponent.matrix4

    var orientation: Quaternion
        get() = worldTransform.getRotation(tempQuaternion)
        set(value) {
            worldTransform.set(value)
        }

    var unitPosition: Vector3
        get() = worldTransform.getTranslation(tempVector1)
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

    fun lookAt(target: Vector3) {
        val direction = tempVector2.set(target).sub(unitPosition).nor()
        val dotProduct = tempVector3.set(defaultFaceDirection).dot(direction)
        val axis = tempVector4.set(defaultFaceDirection).crs(direction).nor()
        worldTransform.rotateRad(axis, acos(dotProduct))
    }

    override fun dispose() {
        gameContext.engine.removeEntity(entity)
        physicComponent.physicInstance.dispose()
    }
}