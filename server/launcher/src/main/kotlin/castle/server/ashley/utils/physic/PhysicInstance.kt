package castle.server.ashley.utils.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

class PhysicInstance internal constructor(
    val constructionInfo: btRigidBody.btRigidBodyConstructionInfo,
    collisionFlag: String,
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
        body.collisionFlags = body.collisionFlags or getCollisionFlag(collisionFlag)
        collisionFilterMask = getFilterMask(collisionFilterMaskList)
        collisionFilterGroup = getFilterGroup(collisionFilterGroupParam)
    }

    override fun dispose() {
        motionState.dispose()
        constructionInfo.dispose()
        body.dispose()
    }

    companion object {
        fun getFilterMask(collisionFilterMaskList: List<Int>): Int {
            return if (collisionFilterMaskList.contains(-1)) -1 else collisionFilterMaskList.fold(0) { acc, i -> acc or 1.shl(i) }
        }

        fun getFilterGroup(collisionFilterGroupParam: Int): Int {
            return 1.shl(collisionFilterGroupParam)
        }

        fun getCollisionFlag(collisionFlag: String) =
            if (collisionFlag.isEmpty()) 0 else btCollisionObject.CollisionFlags::class.java.getField(collisionFlag).getInt(null)
    }
}