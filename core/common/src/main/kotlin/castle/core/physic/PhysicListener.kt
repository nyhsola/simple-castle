package castle.core.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject

interface PhysicListener {
    fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject)
}