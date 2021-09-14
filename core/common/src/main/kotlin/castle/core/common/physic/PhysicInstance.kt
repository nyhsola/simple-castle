package castle.core.common.physic

import com.badlogic.gdx.physics.bullet.collision.CollisionConstants
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

class PhysicInstance(
    val constructionInfo: btRigidBody.btRigidBodyConstructionInfo,
    collisionFlag: List<String>,
    collisionFilterGroupParam: Int,
    collisionFilterMaskList: List<Int>
) : Disposable {
    val body: btRigidBody
    val motionState = MotionState()
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
}