package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;

public class GameLauncher extends ApplicationAdapter {

    private GamePhysicWorld gamePhysicWorld;
    private com.simple.castle.launcher.main.bullet.render.GameEnvironment gameEnvironment;
    private com.simple.castle.launcher.main.bullet.render.GameRenderer gameRenderer;
    private com.simple.castle.launcher.main.bullet.render.GameCamera gameCamera;
    private ModelFactory modelFactory;
    private com.simple.castle.launcher.main.bullet.render.GameOverlay gameOverlay;

    private float spawnTimer;

    @Override
    public void create() {
        gameRenderer = new GameRenderer();
        gameRenderer.create();

        gamePhysicWorld = new GamePhysicWorld();
        gamePhysicWorld.create();

        gameEnvironment = new com.simple.castle.launcher.main.bullet.render.GameEnvironment();
        gameEnvironment.create();

        gameCamera = new com.simple.castle.launcher.main.bullet.render.GameCamera();
        gameCamera.create();

        modelFactory = new ModelFactory();
        modelFactory.create();

        gameOverlay = new com.simple.castle.launcher.main.bullet.render.GameOverlay();
        gameOverlay.create();

        modelFactory.constructMainObjects().forEach(gamePhysicWorld::addRigidBody);

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
        gameRenderer.render(gameCamera, gamePhysicWorld, gameEnvironment);
        gamePhysicWorld.update(gameCamera, delta);
        gameOverlay.render();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        gamePhysicWorld.dispose();
        modelFactory.dispose();
    }
}