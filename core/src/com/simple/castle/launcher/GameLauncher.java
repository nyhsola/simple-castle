package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLauncher extends ApplicationAdapter {
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private ModelBatch modelBatch;
    private Model model;

//    private btCollisionObject surfaceObject;
//    private btCollisionObject sphereObject;

    private GameCamera gameCamera;
    private WorldSettings worldSettings;
    private List<ModelInstance> modelInstances;

    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;

    Tools tools = new Tools();
    private GameModel surfaceGameModel;
    private GameModel cylinder;

    private void preInit() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);

        modelBatch = new ModelBatch();
        model = ModelLoader.loadModel();
    }

    private void postInit() {
        surfaceGameModel = new GameModel("Plane", model);
        cylinder = new GameModel("Cylinder", model);

        gameCamera = new GameCamera(surfaceGameModel.getModelInstance(), cylinder.getModelInstance().nodes.get(0).translation);
        gameCamera.create();

        worldSettings = new WorldSettings();
        worldSettings.create();

        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(gameCamera);
        Gdx.input.setInputProcessor(input);

        modelInstances = new ArrayList<>();
        modelInstances.addAll(Arrays.asList(surfaceGameModel.getModelInstance(), cylinder.getModelInstance()));
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
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(gameCamera.getCamera());
        modelBatch.render(modelInstances, worldSettings.getEnvironment());
        modelBatch.end();

        if (!tools.checkCollision(dispatcher, surfaceGameModel.getBtCollisionObject(), cylinder.getBtCollisionObject())) {
            cylinder.getModelInstance().transform.translate(new Vector3(0, -0.1f, 0));
            cylinder.getBtCollisionObject().setWorldTransform(cylinder.getModelInstance().transform);
        }
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();

        cylinder.dispose();
        surfaceGameModel.dispose();
    }

}
