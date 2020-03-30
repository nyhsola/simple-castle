package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;

public class BGameLauncher extends ApplicationAdapter {

    private BPhysicWorld bPhysicWorld;
    private BEnvironment bEnvironment;
    private BGameRenderer bGameRenderer;
    private BGameCamera bGameCamera;
    private BModelFactory BModelFactory;

    private float spawnTimer;

    @Override
    public void create() {
        bGameRenderer = new BGameRenderer();
        bGameRenderer.create();

        bPhysicWorld = new BPhysicWorld();
        bPhysicWorld.create();

        bEnvironment = new BEnvironment();
        bEnvironment.create();

        bGameCamera = new BGameCamera();
        bGameCamera.create();

        BModelFactory = new BModelFactory();
        BModelFactory.create();

        BModelFactory.constructMap().forEach(bPhysicWorld::addRigidBody);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(bGameCamera.getInputProcessor());

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

        if ((spawnTimer -= delta) < 0) {
//            bPhysicWorld.addRigidBody(BModelFactory.randomObject(bPhysicWorld.objCount()));

            // Check Linear force

            spawnTimer = 3.50f;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        bGameCamera.update();
        bGameRenderer.render(bGameCamera, bPhysicWorld, bEnvironment);

        bPhysicWorld.update(bGameCamera, delta);
    }

    @Override
    public void resize(int width, int height) {
        bGameCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        bPhysicWorld.dispose();
        BModelFactory.dispose();
    }
}