package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.launcher.main.bullet.controller.GameObjectController;
import com.simple.castle.launcher.main.bullet.controller.GameSelectItemController;
import com.simple.castle.launcher.main.bullet.object.unit.SphereUnit;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.render.GameEnvironment;
import com.simple.castle.launcher.main.bullet.render.GameOverlay;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;

public class GameLauncher extends ApplicationAdapter {

    private GamePhysicWorld gamePhysicWorld;
    private GameEnvironment gameEnvironment;
    private GameRenderer gameRenderer;
    private GameCamera gameCamera;
    private ModelFactory modelFactory;
    private GameOverlay gameOverlay;

    private GameObjectController gameObjectController;

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

        modelFactory = new ModelFactory();
        modelFactory.create();

        gameOverlay = new GameOverlay();
        gameOverlay.create();

        gameCamera = new GameCamera(modelFactory.getSurface(), modelFactory.getInitObject().transform.getTranslation(new Vector3()));
        gameCamera.create();

        gameObjectController = new GameObjectController(this);

        gameSelectItemController = new GameSelectItemController(gameCamera, gamePhysicWorld);

        modelFactory.getInitObjects().forEach(gamePhysicWorld::addRigidBody);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameSelectItemController);
        inputMultiplexer.addProcessor(gameObjectController);
        inputMultiplexer.addProcessor(gameCamera);

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

        gameCamera.render();
        gameRenderer.render(gameCamera, gamePhysicWorld, gameEnvironment);
        gamePhysicWorld.update(gameCamera, delta);

        gameOverlay.render(gameCamera, gameSelectItemController.getSelectedObject());
    }

    public void spawn() {
        SphereUnit build = new SphereUnit.Builder(modelFactory.getMainModel()).build();
        gamePhysicWorld.addRigidBody(build);

        build.transform.setTranslation(modelFactory.getSpawner().transform.getTranslation(new Vector3()));
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