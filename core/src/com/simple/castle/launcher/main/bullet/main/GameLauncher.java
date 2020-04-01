package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.simple.castle.launcher.main.bullet.*;

public class GameLauncher extends ApplicationAdapter {

    private PhysicWorld physicWorld;
    private GameEnvironment gameEnvironment;
    private GameRenderer gameRenderer;
    private GameCamera gameCamera;
    private ModelFactory modelFactory;

    private float spawnTimer;

    @Override
    public void create() {
        gameRenderer = new GameRenderer();
        gameRenderer.create();

        physicWorld = new PhysicWorld();
        physicWorld.create();

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        gameCamera = new GameCamera();
        gameCamera.create();

        modelFactory = new ModelFactory();
        modelFactory.create();

        modelFactory.constructMainObjects().forEach(physicWorld::addRigidBody);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameCamera.getInputProcessor());

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

        gameCamera.update();
        gameRenderer.render(gameCamera, physicWorld, gameEnvironment);
        physicWorld.update(gameCamera, delta);
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        physicWorld.dispose();
        modelFactory.dispose();
    }
}