package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
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

public class BPhysicWorld extends ApplicationAdapter {

    private final boolean DEBUG_DRAW = true;

    private BPhysicWorld.MyContactListener contactListener;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private btBroadphaseInterface broadphase;
    private btDynamicsWorld dynamicsWorld;
    private btConstraintSolver constraintSolver;

    private Array<BGameObject> instances;
    private BGameObject ground;

    private float angle, speed = 90f;

    private DebugDrawer debugDrawer;

    public BGameObject getGround() {
        return ground;
    }

    @Override
    public void create() {
        Bullet.init();
        contactListener = new BPhysicWorld.MyContactListener();

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

    public void addGround(BGameObject ground) {
        this.ground = ground;
        addRigidBody(ground);
    }

    public void addRigidBody(BGameObject object) {
        instances.add(object);
        dynamicsWorld.addRigidBody(object.body);
    }

    public void update(BGameCamera camera, float delta) {
//        updateGround(delta);
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

        if (DEBUG_DRAW) {
            debugDrawer.begin(camera.getCam());
            dynamicsWorld.debugDrawWorld();
            debugDrawer.end();
        }
    }

    private void updateGround(float delta) {
        angle = (angle + delta * speed) % 360f;
        getGround().transform.setTranslation(0, MathUtils.sinDeg(angle) * 2.5f, 0f);
    }

    @Override
    public void dispose() {
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
        contactListener.dispose();

        for (BGameObject obj : instances)
            obj.dispose();
        instances.clear();
    }

    public Array<BGameObject> getInstances() {
        return instances;
    }

    public int objCount() {
        return instances.size;
    }

    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                      int index1, boolean match1) {
            if (match0)
                ((ColorAttribute) instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            if (match1)
                ((ColorAttribute) instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            return true;
        }
    }
}
