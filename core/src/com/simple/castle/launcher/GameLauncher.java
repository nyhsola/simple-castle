package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;

import java.util.Arrays;

public class GameLauncher extends ApplicationAdapter {
    private Model model;

    private GameCamera gameCamera;

    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;

    private GameModel surfaceGameModel;
    private GameModel cylinder;

    private PhysicWorld physicWorld;
    private GameRenderer gameRenderer;

    private void preInit() {
        physicWorld = new PhysicWorld();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        model = ModelLoader.loadModel();
    }

    private void postInit() {
        surfaceGameModel = new GameModel("Plane", model);
        cylinder = new GameModel("Cylinder", model);

        physicWorld.addStaticRigidBody(surfaceGameModel);
        physicWorld.addDynamicRigidBody(cylinder, 1);

        gameCamera = new GameCamera(surfaceGameModel.getModelInstance(), cylinder.getModelInstance().nodes.get(0).translation);
        gameCamera.create();

        WorldSettings worldSettings = new WorldSettings();
        worldSettings.create();

        gameRenderer = new GameRenderer(gameCamera, worldSettings,
                Arrays.asList(surfaceGameModel.getModelInstance(), cylinder.getModelInstance()));

        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(gameCamera);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void create() {
        this.preInit();
        this.postInit();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void render() {
        gameRenderer.render();

        physicWorld.update(Gdx.graphics.getDeltaTime());
        physicWorld.debugDraw(gameCamera.getCamera());
    }

    @Override
    public void dispose() {
        model.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();

        cylinder.dispose();
        surfaceGameModel.dispose();
    }

}
