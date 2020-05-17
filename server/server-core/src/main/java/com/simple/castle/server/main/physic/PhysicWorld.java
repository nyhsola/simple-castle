package com.simple.castle.server.main.physic;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.utils.Disposable;

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

    public void addRigidBody(btRigidBody object) {
        dynamicsWorld.addRigidBody(object);
    }

    public void removeRigidBody(btRigidBody object) {
        dynamicsWorld.removeRigidBody(object);
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
