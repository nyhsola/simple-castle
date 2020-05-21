package com.simple.castle.server.physic.world;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.server.physic.unit.PhysicObject;

public class PhysicWorld implements Disposable {
    private final CustomContactListener contactListener;
    private final btCollisionConfiguration collisionConfig;
    private final btDispatcher dispatcher;
    private final btBroadphaseInterface broadphase;
    private final btConstraintSolver constraintSolver;
    private final btDynamicsWorld dynamicsWorld;

    public PhysicWorld() {
        contactListener = new CustomContactListener();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
    }

    public void addRigidBody(PhysicObject object) {
        dynamicsWorld.addRigidBody(object.getBody());
    }

    public void removeRigidBody(PhysicObject object) {
        dynamicsWorld.removeRigidBody(object.getBody());
    }

    public void update(float delta) {
        dynamicsWorld.stepSimulation(Math.min(1f / 30f, delta), 5, 1f / 60f);
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

    private final class CustomContactListener extends ContactListener {

        // TODO: 5/12/2020 check colObj0.isStatic() isKinematic() isActive()
        @Override
        public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
        }

    }
}
