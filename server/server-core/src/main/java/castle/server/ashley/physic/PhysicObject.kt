package castle.server.ashley.physic

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

abstract class PhysicObject internal constructor(private val constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : Disposable {
    val body: btRigidBody
    val motionState = MotionState()

    init {
        constructionInfo.motionState = motionState
        body = btRigidBody(constructionInfo)
    }

    override fun dispose() {
        constructionInfo.dispose()
        body.dispose()
    }
}