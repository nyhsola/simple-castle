package castle.server.ashley.service

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.DebugDrawer
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw
import kotlin.math.min

class PhysicService(private val camera: Camera) {
    private val collisionConfig: btCollisionConfiguration = btDefaultCollisionConfiguration()
    private val dispatcher: btDispatcher = btCollisionDispatcher(collisionConfig)
    private val broadPhase: btBroadphaseInterface = btDbvtBroadphase()
    private val constraintSolver: btConstraintSolver = btSequentialImpulseConstraintSolver()
    private val customDebugDrawer: DebugDrawer = DebugDrawer().apply {
        debugMode = btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE
    }
    private val dynamicsWorld: btDiscreteDynamicsWorld = btDiscreteDynamicsWorld(dispatcher, broadPhase, constraintSolver, collisionConfig).apply {
        gravity = Vector3(0.0f, -10f, 0f); debugDrawer = customDebugDrawer
    }
    private val tempGhost = btGhostObject()
    private val anyHitCallback = AnyHitCallback()

    var isDebug: Boolean = false

    fun renderDebug() {
        if (isDebug) {
            customDebugDrawer.begin(camera)
            dynamicsWorld.debugDrawWorld()
            customDebugDrawer.end()
        }
    }

    fun update(deltaTime: Float) {
        dynamicsWorld.stepSimulation(min(1f / 30f, deltaTime), 5, 1f / 60f)
    }

    fun removeEntity(entity: Entity) {
        val physicObject = PhysicComponent.mapper.get(entity).physicInstance
        dynamicsWorld.removeRigidBody(physicObject.body)
    }

    fun addEntity(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        PhysicComponent.linkPosition(positionComponent, physicComponent)
        dynamicsWorld.addRigidBody(physicComponent.physicInstance.body,
                physicComponent.physicInstance.collisionFilterGroup,
                physicComponent.physicInstance.collisionFilterMask)
    }

    fun dispose(entities: Iterable<Entity>) {
        entities.forEach(action = { entity -> PhysicComponent.mapper.get(entity).dispose() })
        dynamicsWorld.dispose()
        constraintSolver.dispose()
        broadPhase.dispose()
        dispatcher.dispose()
        collisionConfig.dispose()
        tempGhost.dispose()
        anyHitCallback.dispose()
    }

    fun hasCollisions(position: Vector3, halfBox: Vector3): Boolean {
        val shape = btBoxShape(halfBox)
        tempGhost.collisionShape = shape
        tempGhost.worldTransform = Matrix4().setTranslation(position)
        dynamicsWorld.collisionWorld.contactTest(tempGhost, anyHitCallback)
        shape.dispose()
        return anyHitCallback.isAnyHit
    }

    internal class AnyHitCallback : ContactResultCallback() {
        var isAnyHit: Boolean = false
            get() {
                val saved = field
                isAnyHit = false
                return saved
            }

        override fun addSingleResult(cp: btManifoldPoint?, colObj0Wrap: btCollisionObjectWrapper?, partId0: Int, index0: Int,
                                     colObj1Wrap: btCollisionObjectWrapper?, partId1: Int, index1: Int): Float {
            isAnyHit = true
            return 0.0f
        }
    }
}