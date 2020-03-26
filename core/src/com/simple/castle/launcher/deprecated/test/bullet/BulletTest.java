package com.simple.castle.launcher.deprecated.test.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class BulletTest extends ApplicationAdapter {
    final static short GROUND_FLAG = 1 << 8;
    final static short OBJECT_FLAG = 1 << 9;
    final static short ALL_FLAG = -1;

    BPhysicWorld bPhysicWorld;

    ModelFabric modelFabric;

    PerspectiveCamera cam;
    CameraInputController camController;
    ModelBatch modelBatch;
    Environment environment;
    float spawnTimer;
    float angle, speed = 90f;

    @Override
    public void create() {
        bPhysicWorld = new BPhysicWorld();
        bPhysicWorld.create();

        modelFabric = new ModelFabric();

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 7f, 10f);
        cam.lookAt(0, 4f, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        BGameObject object = modelFabric.getConstructorArrayMap().get("ground").construct();
        object.body.setCollisionFlags(object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        bPhysicWorld.addGround(object);
        object.body.setContactCallbackFlag(GROUND_FLAG);
        object.body.setContactCallbackFilter(0);
        object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }

    public void spawn() {
        BGameObject obj = modelFabric.getConstructorArrayMap().values[1 + MathUtils.random(modelFabric.getConstructorArrayMap().size - 2)].construct();
        obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
        obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f));
        obj.body.proceedToTransform(obj.transform);
        obj.body.setUserValue(bPhysicWorld.objCount());
        obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        bPhysicWorld.addRigidBody(obj);
        obj.body.setContactCallbackFlag(OBJECT_FLAG);
        obj.body.setContactCallbackFilter(GROUND_FLAG);
    }

    @Override
    public void render() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

        angle = (angle + delta * speed) % 360f;
        bPhysicWorld.getGround().transform.setTranslation(0, MathUtils.sinDeg(angle) * 2.5f, 0f);

        bPhysicWorld.update(delta);

        if ((spawnTimer -= delta) < 0) {
            spawn();
            spawnTimer = 1.5f;
        }

        camController.update();

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(bPhysicWorld.getInstances(), environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        bPhysicWorld.dispose();
        modelBatch.dispose();
        modelFabric.dispose();
    }
}