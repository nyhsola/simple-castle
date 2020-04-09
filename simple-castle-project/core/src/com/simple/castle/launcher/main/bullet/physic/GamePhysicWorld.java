package com.simple.castle.launcher.main.bullet.physic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;

public class GamePhysicWorld extends ApplicationAdapter {

    private final boolean DEBUG_DRAW = true;

    private GamePhysicWorld.MyContactListener contactListener;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private btBroadphaseInterface broadphase;
    private btConstraintSolver constraintSolver;

    private btDynamicsWorld dynamicsWorld;

    private Array<GameObject> instances;

    private DebugDrawer debugDrawer;

    @Override
    public void create() {
        Bullet.init();

        contactListener = new MyContactListener();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));

        instances = new Array<>();

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    public void addRigidBody(GameObject object) {
        instances.add(object);
        dynamicsWorld.addRigidBody(object.body);
    }

    public void update(GameCamera camera, float delta) {
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

        if (DEBUG_DRAW) {
            debugDrawer.begin(camera.getPerspectiveCamera());
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
        for (GameObject obj : instances) {
            obj.dispose();
        }
        instances.clear();
    }

    public Array<GameObject> getInstances() {
        return instances;
    }

    public int objCount() {
        return instances.size;
    }

    private static class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                      int index1, boolean match1) {
//            if (match0)
//                ((ColorAttribute) instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
//            if (match1)
//                ((ColorAttribute) instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            return true;
        }
    }
}
