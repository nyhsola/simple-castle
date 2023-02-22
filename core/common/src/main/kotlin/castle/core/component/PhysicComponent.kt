package castle.core.component

import castle.core.json.TemplateJson
import castle.core.physic.MotionState
import castle.core.physic.PhysicTools
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

class PhysicComponent(
    modelInstance: ModelInstance,
    templateJson: TemplateJson
) : Component, Disposable {
    private val motionState = MotionState()
    private val constructionInfo = btRigidBody.btRigidBodyConstructionInfo(templateJson.mass, motionState, templateJson.shape.build(modelInstance))
    val body: btRigidBody = btRigidBody(constructionInfo)
    val collisionFilterMask: Int = PhysicTools.getFilterMask(templateJson.collisionFilterMask)
    val collisionFilterGroup: Int = PhysicTools.getFilterGroup(templateJson.collisionFilterGroup)

    init {
        body.activationState = CollisionConstants.DISABLE_DEACTIVATION
        body.collisionFlags = body.collisionFlags or PhysicTools.getCollisionFlag(templateJson.collisionFlag)
    }

    override fun dispose() {
        motionState.dispose()
        constructionInfo.dispose()
        body.dispose()
    }

    companion object {
        val mapper: ComponentMapper<PhysicComponent> = ComponentMapper.getFor(PhysicComponent::class.java)

        fun postConstruct(positionComponent: PositionComponent, physicComponent: PhysicComponent) {
            physicComponent.motionState.transform = positionComponent.matrix4
            physicComponent.body.motionState = physicComponent.motionState
            val mass = physicComponent.constructionInfo.mass
            if (mass != 0.0f) {
                physicComponent.body.collisionShape.calculateLocalInertia(mass, Vector3.Zero)
            }
        }
    }
}