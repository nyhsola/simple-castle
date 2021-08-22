package castle.core.game.`object`.unit

import castle.core.common.component.AnimationComponent
import castle.core.common.component.PositionComponent
import castle.core.common.component.RenderComponent
import castle.core.game.GameContext
import castle.core.common.json.Constructor
import castle.core.common.component.PhysicComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g3d.utils.AnimationController
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import java.util.*
import kotlin.math.acos

open class GameObject(
    private val constructor: Constructor,
    private val gameContext: GameContext,
) : Disposable {
    protected companion object {
        private val defaultFaceDirection: Vector3 = Vector3(1f, 0f, 0f)
        private val animationListener = object : AnimationController.AnimationListener {
            override fun onEnd(animation: AnimationController.AnimationDesc) {
            }
            override fun onLoop(animation: AnimationController.AnimationDesc) {
            }
        }
        val right: Vector3 = Vector3(0f, 1f, 0f)
        val left: Vector3 = Vector3(0f, -1f, 0f)
        val zero: Vector3 = Vector3(0f, 0f, 0f)
    }

    private val entity: Entity = gameContext.engine.createEntity()
    private val positionComponent: PositionComponent = PositionComponent(constructor.getMatrix4())
    private val renderComponent: RenderComponent = RenderComponent(positionComponent, constructor)
    private val physicComponent: PhysicComponent = PhysicComponent(constructor)
    private val animationComponent: AnimationComponent = AnimationComponent(renderComponent)
    private val tempVector1: Vector3 = Vector3()
    private val tempVector2: Vector3 = Vector3()
    private val tempVector3: Vector3 = Vector3()
    private val tempVector4: Vector3 = Vector3()
    private val tempVector5: Vector3 = Vector3()
    private val tempQuaternion: Quaternion = Quaternion()

    init {
        entity.apply {
            add(positionComponent)
            add(renderComponent)
            add(physicComponent)
            add(renderComponent)
            add(animationComponent)
        }
        gameContext.engine.addEntity(entity)
    }

    val worldTransform: Matrix4
        get() = positionComponent.matrix4

    val uuid: String
        get() = physicComponent.physicInstance.body.userData as String

    val faceDirection: Vector3
        get() = orientation.transform(tempVector5.set(defaultFaceDirection))

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
            physicComponent.physicInstance.body.activate()
            physicComponent.physicInstance.body.angularVelocity = value
        }

    var linearVelocity: Vector3
        get() = physicComponent.physicInstance.body.linearVelocity
        set(value) {
            physicComponent.physicInstance.body.activate()
            physicComponent.physicInstance.body.linearVelocity = value
        }

    fun lookAt(target: Vector3) {
        val direction = tempVector2.set(target).sub(unitPosition).nor()
        val dotProduct = tempVector3.set(defaultFaceDirection).dot(direction)
        val axis = tempVector4.set(defaultFaceDirection).crs(direction).nor()
        worldTransform.rotateRad(axis, acos(dotProduct))
    }

    fun playAnimation(animation: String, speed: Float) {
        val animationFull = constructor.armature + "|" + animation
        animationComponent.animationController.paused = false
        animationComponent.animationController.setAnimation(animationFull, -1, speed, animationListener)
    }

    fun stopAnimation() {
        animationComponent.animationController.paused = true
    }

    override fun dispose() {
        gameContext.engine.removeEntity(entity)
    }
}