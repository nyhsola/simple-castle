package castle.core.component

import castle.core.physic.MotionState
import castle.core.physic.PhysicTools
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

class PhysicComponent(
        val constructionInfo: btRigidBody.btRigidBodyConstructionInfo,
        collisionFlag: List<String>,
        collisionFilterGroupParam: Int,
        collisionFilterMaskList: List<Int>
) : Component, Disposable {
    val body: btRigidBody
    private val motionState = MotionState()
    val collisionFilterMask: Int
    val collisionFilterGroup: Int

    init {
        constructionInfo.motionState = motionState
        body = btRigidBody(constructionInfo)
        body.activationState = CollisionConstants.DISABLE_DEACTIVATION
        body.collisionFlags = body.collisionFlags or PhysicTools.getCollisionFlag(collisionFlag)
        collisionFilterMask = PhysicTools.getFilterMask(collisionFilterMaskList)
        collisionFilterGroup = PhysicTools.getFilterGroup(collisionFilterGroupParam)
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