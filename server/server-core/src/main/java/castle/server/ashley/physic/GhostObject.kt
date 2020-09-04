package castle.server.ashley.physic

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody

open class GhostObject(constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : PhysicObject(constructionInfo) {
    init {
        body.collisionFlags = body.collisionFlags or btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE
    }
}