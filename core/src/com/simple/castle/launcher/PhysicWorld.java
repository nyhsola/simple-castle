package com.simple.castle.launcher;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

public class PhysicWorld {
    private btDynamicsWorld world;
    private DebugDrawer debugDrawer;

    private btCollisionConfiguration btCollisionConfiguration;
    private btDispatcher btDispatcher;

    private btBroadphaseInterface btBroadphaseInterface;
    private btConstraintSolver btConstraintSolver;

    public PhysicWorld() {
        Bullet.init();

        btCollisionConfiguration = new btDefaultCollisionConfiguration();
        btDispatcher = new btCollisionDispatcher(btCollisionConfiguration);

        btBroadphaseInterface = new btDbvtBroadphase();
        btConstraintSolver = new btSequentialImpulseConstraintSolver();

        world = new btDiscreteDynamicsWorld(btDispatcher, btBroadphaseInterface, btConstraintSolver, btCollisionConfiguration);

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        world.setDebugDrawer(debugDrawer);
        world.setGravity(new Vector3(0, -9.8f, 0));
    }

    public void update(float delta) {
        world.stepSimulation(delta);
    }

    public btRigidBody addRigidBody(GameModel gameModel) {
        btCollisionShape shape = Bullet.obtainStaticNodeShape(gameModel.getModelInstance().nodes.get(0), true);
        btRigidBody body = new btRigidBody(0, gameModel.getMotionState(), shape);
        body.activate();
        world.addRigidBody(body);
        return body;
    }

    public btRigidBody addRigidBodyPhysic(GameModel gameModel, float mass) {
        btCollisionShape shape = Bullet.obtainStaticNodeShape(gameModel.getModelInstance().nodes.get(0), true);
        btRigidBody body = new btRigidBody(100, gameModel.getMotionState(), shape);

        Vector3 inertia = new Vector3();
        body.getCollisionShape().calculateLocalInertia(mass, inertia);
        body.setMassProps(mass, inertia);
        body.updateInertiaTensor();
        body.activate();

        gameModel.setBtRigidBody(body);
        world.addRigidBody(body);
        return body;
    }

    public void debugDraw(Camera camera) {
        debugDrawer.begin(camera);
        world.debugDrawWorld();
        debugDrawer.end();
    }
}
