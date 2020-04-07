package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;
import com.simple.castle.launcher.main.bullet.render.*;

public class GameLauncher extends ApplicationAdapter {

    private GamePhysicWorld gamePhysicWorld;
    private GameEnvironment gameEnvironment;
    private GameRenderer gameRenderer;
    private GameCamera gameCamera;
    private ModelFactory modelFactory;
    private GameOverlay gameOverlay;

    private GameSelectItemController gameSelectItemController;

    private float spawnTimer;

    @Override
    public void create() {
        gameRenderer = new GameRenderer();
        gameRenderer.create();

        gamePhysicWorld = new GamePhysicWorld();
        gamePhysicWorld.create();

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        gameCamera = new GameCamera();
        gameCamera.create();

        modelFactory = new ModelFactory();
        modelFactory.create();

        gameOverlay = new GameOverlay();
        gameOverlay.create();

        gameSelectItemController = new GameSelectItemController(gameCamera, gamePhysicWorld);

        modelFactory.constructMainObjects().forEach(gamePhysicWorld::addRigidBody);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameSelectItemController);
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

        gameOverlay.render(gameCamera, gameSelectItemController.getGameObject());
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