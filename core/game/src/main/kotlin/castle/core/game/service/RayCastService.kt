package castle.core.game.service

import castle.core.common.service.CameraService
import castle.core.physic.PhysicInstance
import castle.core.physic.service.PhysicService
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.utils.Disposable

class RayCastService(
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : Disposable {
    private val meters = 50f
    private var rayFrom = Vector3()
    private var rayTo = Vector3()
    private var rayTestCB: ClosestRayResultCallback = ClosestRayResultCallback(Vector3.Zero, Vector3.Z)

    fun rayCast(x: Float, y: Float): btCollisionObject? {
        val ray = cameraService.currentCamera.camera.getPickRay(x, y)

        rayFrom.set(ray.origin)
        rayTo.set(ray.direction).scl(meters).add(rayFrom)

        rayTestCB.collisionObject = null
        rayTestCB.closestHitFraction = 1f
        rayTestCB.setRayFromWorld(rayFrom)
        rayTestCB.setRayToWorld(rayTo)
        rayTestCB.collisionFilterGroup = PhysicInstance.getFilterGroup(3)
        rayTestCB.collisionFilterMask = PhysicInstance.getFilterMask(listOf(2, 3))

        physicService.dynamicsWorld.collisionWorld.rayTest(rayFrom, rayTo, rayTestCB)

        return if (rayTestCB.hasHit()) rayTestCB.collisionObject else null
    }

    override fun dispose() {
        rayTestCB.dispose()
    }
}