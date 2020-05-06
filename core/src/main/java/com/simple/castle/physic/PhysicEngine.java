package com.simple.castle.physic;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.render.GameCamera;

import java.util.ArrayList;
import java.util.List;

public class PhysicEngine implements Disposable {

    private final PhysicEngine.MyContactListener contactListener;
    private final btCollisionConfiguration collisionConfig;
    private final btDispatcher dispatcher;
    private final btBroadphaseInterface broadphase;
    private final btConstraintSolver constraintSolver;
    private final btDynamicsWorld dynamicsWorld;
    private final DebugDrawer debugDrawer;

    public PhysicEngine() {
        contactListener = new PhysicEngine.MyContactListener();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    public void addRigidBody(AbstractGameObject object) {
        dynamicsWorld.addRigidBody(object.body);
    }

    public void removeRigidBody(AbstractGameObject object) {
        dynamicsWorld.removeRigidBody(object.body);
    }

    public void update(GameCamera camera, float delta, boolean debugDraw) {
        dynamicsWorld.stepSimulation(Math.min(1f / 30f, delta), 5, 1f / 60f);
        if (debugDraw) {
            debugDrawer.begin(camera);
            dynamicsWorld.debugDrawWorld();
            debugDrawer.end();
        }
    }

    @Override
    public void dispose() {
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
        contactListener.dispose();
    }

    public void addContactListener(CollisionEvent collisionEvent) {
        contactListener.addListener(collisionEvent);
    }

    private static class MyContactListener extends ContactListener {
        private final List<CollisionEvent> collisionEventList = new ArrayList<>();

        public void addListener(CollisionEvent collisionEvent) {
            collisionEventList.add(collisionEvent);
        }

        @Override
        public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
            for (CollisionEvent collisionEvent : collisionEventList) {
                collisionEvent.collisionEvent(colObj0, colObj1);
            }
        }

    }
}
