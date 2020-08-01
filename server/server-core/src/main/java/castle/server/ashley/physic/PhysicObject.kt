package castle.server.ashley.physic

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

abstract class PhysicObject internal constructor(private val constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : Disposable {
    val body: btRigidBody = btRigidBody(constructionInfo)
    val motionState: MotionState = MotionState()

    override fun dispose() {
        motionState.dispose()
        constructionInfo.dispose()
        body.dispose()
    }
}