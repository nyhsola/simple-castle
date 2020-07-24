package com.simple.castle.server.kt.physic

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.DebugDrawer
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.core.kt.render.BaseCamera
import java.util.function.Consumer
import kotlin.math.min

class PhysicWorld(physicObjects: List<PhysicObject>) : Disposable {
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

    init {
        physicObjects.forEach(Consumer { physicObject -> addRigidBody(physicObject) })
    }

    fun addRigidBody(`object`: PhysicObject) {
        dynamicsWorld.addRigidBody(`object`.body)
    }

    fun removeRigidBody(`object`: PhysicObject) {
        dynamicsWorld.removeRigidBody(`object`.body)
    }

    fun update(delta: Float) {
        dynamicsWorld.stepSimulation(min(1f / 30f, delta), 5, 1f / 60f)
    }

    fun debugDraw(camera: BaseCamera) {
        customDebugDrawer.begin(camera)
        dynamicsWorld.debugDrawWorld()
        customDebugDrawer.end()
    }

    override fun dispose() {
        dynamicsWorld.dispose()
        constraintSolver.dispose()
        broadPhase.dispose()
        dispatcher.dispose()
        collisionConfig.dispose()
        contactListener.dispose()
    }

    private inner class CustomContactListener : ContactListener() {
        // TODO: 5/12/2020 check colObj0.isStatic() isKinematic() isActive()
        override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {}
    }

}