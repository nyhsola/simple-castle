package castle.server.ashley.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

class PhysicObject internal constructor(private val constructionInfo: btRigidBody.btRigidBodyConstructionInfo, collisionFlag: String,
                                        collisionFilterGroupParam: Int, collisionFilterMaskList: List<Int>) : Disposable {
    val body: btRigidBody
    val motionState = MotionState()
    val collisionFilterMask: Int
    val collisionFilterGroup: Int

    init {
        constructionInfo.motionState = motionState
        body = btRigidBody(constructionInfo)
        body.collisionFlags = body.collisionFlags or getCollisionFlag(collisionFlag)
        collisionFilterMask = if (collisionFilterMaskList.contains(-1)) -1 else collisionFilterMaskList.fold(0) { acc, i -> acc or 1.shl(i) }
        collisionFilterGroup = 1.shl(collisionFilterGroupParam)
    }

    override fun dispose() {
        constructionInfo.dispose()
        body.dispose()
    }

    private fun getCollisionFlag(collisionFlag: String) = if (collisionFlag.isEmpty()) 0 else btCollisionObject.CollisionFlags::class.java.getField(
            collisionFlag).getInt(null)
}