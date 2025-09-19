package castle.core.service

import castle.core.physic.PhysicTools
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.utils.Disposable
import org.koin.core.annotation.Single

@Single
class RayCastService(
    private val physicService: PhysicService,
    private val cameraService: CameraService
) : Disposable {
    private val meters = 50f
    private val rayFrom = Vector3()
    private val rayTo = Vector3()
    private val rayTestCB: ClosestRayResultCallback = ClosestRayResultCallback(rayFrom, rayTo)

    fun rayCastObject(x: Float, y: Float): btCollisionObject? {
        val ray = cameraService.currentCamera.camera.getPickRay(x, y)
        rayFrom.set(ray.origin)
        rayTo.set(ray.direction).scl(meters).add(rayFrom)
        rayTestCB.collisionObject = null
        rayTestCB.closestHitFraction = 1f
        rayTestCB.setRayFromWorld(rayFrom)
        rayTestCB.setRayToWorld(rayTo)
        rayTestCB.collisionFilterGroup = PhysicTools.getFilterGroup(1)
        rayTestCB.collisionFilterMask = PhysicTools.getFilterMask(listOf(4))
        physicService.dynamicsWorld.collisionWorld.rayTest(rayFrom, rayTo, rayTestCB)

        if (rayTestCB.hasHit()) {
            val out = Vector3()
            rayTestCB.getHitPointWorld(out)
            println(out)
        }

        return if (rayTestCB.hasHit()) rayTestCB.collisionObject else null
    }

    fun rayCastPosition(x: Float, y: Float, out: Vector3 = Vector3()): Vector3? {
        val ray = cameraService.currentCamera.camera.getPickRay(x, y)
        rayFrom.set(ray.origin)
        rayTo.set(ray.direction).scl(meters).add(rayFrom)
        rayTestCB.collisionObject = null
        rayTestCB.closestHitFraction = 1f
        rayTestCB.setRayFromWorld(rayFrom)
        rayTestCB.setRayToWorld(rayTo)
        rayTestCB.collisionFilterGroup = PhysicTools.getFilterGroup(1)
        rayTestCB.collisionFilterMask = PhysicTools.getFilterMask(listOf(1))
        physicService.dynamicsWorld.collisionWorld.rayTest(rayFrom, rayTo, rayTestCB)

        rayTestCB.getHitPointWorld(out)
        return if (rayTestCB.hasHit()) out else null
    }

    override fun dispose() {
        rayTestCB.dispose()
    }
}