package castle.core.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

open class PhysicInstance(
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
            if (collisionFilterMaskList.contains(-1)) {
                return -1
            }
            return collisionFilterMaskList.fold(0) { acc, i -> acc or 1.shl(i) }
        }

        fun getFilterGroup(collisionFilterGroupParam: Int): Int {
            return 1.shl(collisionFilterGroupParam)
        }

        fun getCollisionFlag(collisionFlag: List<String>): Int {
            if (collisionFlag.isEmpty()) {
                return 0
            }
            return collisionFlag.fold(0) { acc, i ->
                acc or btCollisionObject.CollisionFlags::class.java.getField(i).getInt(null)
            }
        }
    }
}