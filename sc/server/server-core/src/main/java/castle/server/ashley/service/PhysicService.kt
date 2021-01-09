package castle.server.ashley.service

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.DebugDrawer
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
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
    private val tempArray = IntArray(1)

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
        val physicObject = PhysicComponent.mapper.get(entity).physicObject
        dynamicsWorld.removeRigidBody(physicObject.body)
    }

    fun addEntity(entity: Entity) {
        val positionComponent = PositionComponent.mapper.get(entity)
        val physicComponent = PhysicComponent.mapper.get(entity)
        PhysicComponent.link(positionComponent, physicComponent)
        dynamicsWorld.addRigidBody(physicComponent.physicObject.body,
                physicComponent.physicObject.collisionFilterGroup,
                physicComponent.physicObject.collisionFilterMask)
    }

    fun dispose(entities: Iterable<Entity>) {
        entities.forEach(action = { entity -> PhysicComponent.mapper.get(entity).dispose() })
        dynamicsWorld.dispose()
        constraintSolver.dispose()
        broadPhase.dispose()
        dispatcher.dispose()
        collisionConfig.dispose()
    }

    fun hasCollisions(collisionObject: btRigidBody): Boolean {
        dynamicsWorld.dispatcher.dispatchAllCollisionPairs(broadPhase.overlappingPairCache, dynamicsWorld.dispatchInfo, dynamicsWorld.dispatcher)
        val pairArray = dynamicsWorld.broadphase.overlappingPairCache.overlappingPairArray
        return pairArray.getCollisionObjectsValue(tempArray, collisionObject) > 0
    }

/*
        This should work, need to find out
        dynamicsWorld.addRigidBody(collisionObject)
        dynamicsWorld.dispatcher.dispatchAllCollisionPairs(
            broadPhase.overlappingPairCache,
            dynamicsWorld.dispatchInfo,
            dynamicsWorld.dispatcher
        )
        val pairArray = dynamicsWorld.broadphase.overlappingPairCache.overlappingPairArray
        val pairArraySize = pairArray.size()
        if (pairArraySize > 0) {
            val intArr = IntArray(pairArraySize)
            val obj = com.badlogic.gdx.utils.Array<btCollisionObject>(5)
            val arr = pairArray.getCollisionObjects(obj, collisionObject, intArr)
            if (arr.size > 0) {
                print("Collisions with $collisionObject - ${arr.size}")
            }
        }
        dynamicsWorld.removeRigidBody(collisionObject)

        Check all combinations
        val size = arr.size()
        for (i in 0 until size) {
            val pair = arr.at(i)
            val proxy1 = btBroadphaseProxy.obtain(pair.pProxy0.cPointer, false)
            val proxy2 = btBroadphaseProxy.obtain(pair.pProxy1.cPointer, false)
            val collisionPair = dynamicsWorld.pairCache.findPair(proxy1, proxy2)
            val algorithm = collisionPair.algorithm
            if (algorithm != null) {
                val manifoldArray = btPersistentManifoldArray()
                algorithm.getAllContactManifolds(manifoldArray)
                val manifoldArraySize = manifoldArray.size()
                for (j in 0 until manifoldArraySize) {
                    val manifold = manifoldArray.atConst(j)
                    if (manifold != null && manifold.body0 != null && manifold.body1 != null) {
                        val data0 = manifold.body0.userData
                        val data1 = manifold.body1.userData

                        if (data0 == "ghost-test" || data1 == "ghost-test") {
                            print(1)
                        }
                    }
                }
            }
        }
    }
    private class CustomContactListener : ContactListener() {
        override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {
        }
    }
 */
}