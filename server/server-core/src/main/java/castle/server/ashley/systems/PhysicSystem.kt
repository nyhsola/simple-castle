package castle.server.ashley.systems

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.DebugDrawer
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw
import kotlin.math.min

class PhysicSystem(private val camera: Camera) : IteratingSystemAdapter(Family.all(PositionComponent::class.java, PhysicComponent::class.java).get()), EntityListener {

    private val contactListener: CustomContactListener = CustomContactListener()
    private val collisionConfig: btCollisionConfiguration = btDefaultCollisionConfiguration()
    private val dispatcher: btDispatcher = btCollisionDispatcher(collisionConfig)
    private val broadPhase: btBroadphaseInterface = btDbvtBroadphase()
    private val constraintSolver: btConstraintSolver = btSequentialImpulseConstraintSolver()
    private val customDebugDrawer: DebugDrawer = DebugDrawer()
            .apply {
                debugMode = btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE
            }
    private val dynamicsWorld: btDynamicsWorld = btDiscreteDynamicsWorld(dispatcher, broadPhase, constraintSolver, collisionConfig)
            .apply {
                gravity = Vector3(0.0f, -10f, 0f)
                debugDrawer = customDebugDrawer
            }
    private var isDebug: Boolean = false

    override fun render(delta: Float) {
        if (isDebug) {
            customDebugDrawer.begin(camera)
            dynamicsWorld.debugDrawWorld()
            customDebugDrawer.end()
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.F1) {
            isDebug = !isDebug
        }
        return super.keyDown(keycode)
    }

    override fun update(deltaTime: Float) {
        dynamicsWorld.stepSimulation(min(1f / 30f, deltaTime), 5, 1f / 60f)
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(PositionComponent::class.java, PhysicComponent::class.java).get(), this)
        super.addedToEngine(engine)
    }

    override fun entityRemoved(entity: Entity) {
        val physicObject = PhysicComponent.mapper.get(entity).physicObject
        dynamicsWorld.removeRigidBody(physicObject.body)
    }

    override fun entityAdded(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        PhysicComponent.link(positionComponent, physicComponent)
        dynamicsWorld.addRigidBody(physicComponent.physicObject.body)
    }

    override fun dispose() {
        entities.forEach(action = { entity -> PhysicComponent.mapper.get(entity).dispose() })
        dynamicsWorld.dispose()
        constraintSolver.dispose()
        broadPhase.dispose()
        dispatcher.dispose()
        collisionConfig.dispose()
        contactListener.dispose()
    }

    private class CustomContactListener : ContactListener() {
        override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {
        }
    }

}