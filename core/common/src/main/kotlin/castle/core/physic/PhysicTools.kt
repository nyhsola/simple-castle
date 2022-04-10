package castle.core.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject

class PhysicTools {
    companion object {
        fun getFilterMask(collisionFilterMaskList: List<Int>): Int {
            if (collisionFilterMaskList.contains(-1)) return -1
            return collisionFilterMaskList.fold(0) { acc, i -> acc or 1.shl(i) }
        }

        fun getFilterGroup(collisionFilterGroupParam: Int): Int {
            return 1.shl(collisionFilterGroupParam)
        }

        fun getCollisionFlag(collisionFlag: List<String>): Int {
            if (collisionFlag.isEmpty()) return 0
            return collisionFlag.fold(0) { acc, i -> acc or extractFlag(i) }
        }

        private fun extractFlag(i: String) = btCollisionObject.CollisionFlags::class.java.getField(i).getInt(null)
    }
}