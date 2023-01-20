package castle.core.service

import castle.core.component.PhysicComponent
import castle.core.component.PositionComponent
import castle.core.physic.PhysicListener
import castle.core.physic.PhysicTools
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.DebugDrawer
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw
import org.koin.core.annotation.Single

@Single
class PhysicService(private val cameraService: CameraService) {
    private val contactListener: CustomContactListener = CustomContactListener()
    private val collisionConfig: btCollisionConfiguration = btDefaultCollisionConfiguration()
    private val dispatcher: btDispatcher = btCollisionDispatcher(collisionConfig)
    private val broadPhase: btBroadphaseInterface = btDbvtBroadphase()
    private val constraintSolver: btConstraintSolver = btSequentialImpulseConstraintSolver()
    private val customDebugDrawer: DebugDrawer = DebugDrawer().apply {
        debugMode = btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE
    }
    private val tempGhost = btGhostObject()
    private val tempMatrix = Matrix4()
    private val anyHitCallback = AnyHitCallback()
    private val physicListeners: MutableList<PhysicListener> = ArrayList()

    var debugEnabled: Boolean = false
    val dynamicsWorld: btDiscreteDynamicsWorld =
            btDiscreteDynamicsWorld(dispatcher, broadPhase, constraintSolver, collisionConfig).apply {
                gravity = Vector3(0.0f, -10f, 0f)
                debugDrawer = customDebugDrawer
            }

    fun update(deltaTime: Float) {
        if (debugEnabled) {
            customDebugDrawer.begin(cameraService.currentCamera.camera)
            dynamicsWorld.debugDrawWorld()
            customDebugDrawer.end()
        }
        dynamicsWorld.stepSimulation(deltaTime, 5, 1f / 60f)
    }

    fun removeEntity(entity: Entity) {
        val physicComponent = PhysicComponent.mapper.get(entity)
        dynamicsWorld.removeRigidBody(physicComponent.body)
        physicComponent.dispose()
    }

    fun addEntity(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        PhysicComponent.postConstruct(positionComponent, physicComponent)
        dynamicsWorld.addRigidBody(
                physicComponent.body,
                physicComponent.collisionFilterGroup,
                physicComponent.collisionFilterMask
        )
    }

    fun dispose() {
        dynamicsWorld.dispose()
        constraintSolver.dispose()
        broadPhase.dispose()
        dispatcher.dispose()
        collisionConfig.dispose()
        tempGhost.dispose()
        anyHitCallback.dispose()
        contactListener.dispose()
    }

    fun hasCollisions(position: Vector3, shape: btCollisionShape): Boolean {
        tempGhost.collisionShape = shape
        tempGhost.worldTransform = tempMatrix.setTranslation(position)
        dynamicsWorld.collisionWorld.contactTest(tempGhost, anyHitCallback)
        return anyHitCallback.isAnyHit
    }

    fun addListener(physicListener: PhysicListener) {
        physicListeners.add(physicListener)
    }

    fun removeListener(physicListener: PhysicListener) {
        physicListeners.remove(physicListener)
    }

    private class AnyHitCallback : ContactResultCallback() {
        var isAnyHit: Boolean = false
            get() {
                val saved = field
                isAnyHit = false
                return saved
            }

        init {
            collisionFilterGroup = PhysicTools.getFilterGroup(8)
        }

        override fun addSingleResult(
                cp: btManifoldPoint?,
                colObj0Wrap: btCollisionObjectWrapper?,
                partId0: Int,
                index0: Int,
                colObj1Wrap: btCollisionObjectWrapper?,
                partId1: Int,
                index1: Int
        ): Float {
            isAnyHit = true
            return 0.0f
        }
    }

    private inner class CustomContactListener : ContactListener() {
        override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {
            if (colObj0.userData !is Entity || colObj1.userData !is Entity) return
            val entity1 = colObj0.userData as Entity
            val entity2 = colObj1.userData as Entity
            physicListeners.forEach { it.onContactStarted(entity1, entity2) }
        }

        override fun onContactEnded(colObj0: btCollisionObject, colObj1: btCollisionObject) {
            if (colObj0.userData !is Entity || colObj1.userData !is Entity) return
            val entity1 = colObj0.userData as Entity
            val entity2 = colObj1.userData as Entity
            physicListeners.forEach { it.onContactEnded(entity1, entity2) }
        }
    }
}