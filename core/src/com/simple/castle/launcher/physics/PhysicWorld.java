package com.simple.castle.launcher.physics;

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

    private PhysicContactListener physicContactListener;

    public PhysicWorld() {
        Bullet.init();

        physicContactListener = new PhysicContactListener();

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
        world.performDiscreteCollisionDetection();
        world.stepSimulation(delta);
    }

    public void addKinematicObject(PhysicModel physicModel) {
        btRigidBody body = obtainCollisionShapeFromModel(physicModel);

        body.setUserValue(2);
        body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        body.activate();

        world.addRigidBody(body);
    }

    public void addDynamicObject(PhysicModel physicModel, float mass) {
        btRigidBody body = obtainCollisionShapeFromModel(physicModel);

        body.setUserValue(2);
        body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        Vector3 inertia = new Vector3();
        body.getCollisionShape().calculateLocalInertia(mass, inertia);
        body.setMassProps(mass, inertia);
        body.updateInertiaTensor();
        body.activate();

        world.addRigidBody(body);
    }

    public void debugDraw(Camera camera) {
        debugDrawer.begin(camera);
        world.debugDrawWorld();
        debugDrawer.end();
    }

    private btRigidBody obtainCollisionShapeFromModel(PhysicModel model) {
        btCollisionShape shape = Bullet.obtainStaticNodeShape(model.getModelInstance().nodes.get(0), true);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(1, model.getMotionState(), shape, Vector3.Zero);
        return new btRigidBody(info);
    }
}
