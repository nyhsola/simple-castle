package com.simple.castle.launcher.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.Model;
import com.simple.castle.launcher.physics.PhysicModel;
import com.simple.castle.launcher.physics.PhysicWorld;
import com.simple.castle.launcher.utils.ModelLoader;

import java.util.Arrays;

public class GameLauncher extends ApplicationAdapter {
    private Model model;

    private GameCamera gameCamera;

    private PhysicModel surfacePhysicModel;
    private PhysicModel cylinder;

    private PhysicWorld physicWorld;
    private GameRenderer gameRenderer;

    @Override
    public void create() {
        physicWorld = new PhysicWorld();
        model = ModelLoader.loadModel();

        surfacePhysicModel = new PhysicModel("Plane", model);
        cylinder = new PhysicModel("Cylinder", model);

        physicWorld.addKinematicObject(surfacePhysicModel);
        physicWorld.addDynamicObject(cylinder, 1);

        gameCamera = new GameCamera(surfacePhysicModel.getModelInstance(), cylinder.getModelInstance().nodes.get(0).translation);
        gameCamera.create();

        GameWorldSettings gameWorldSettings = new GameWorldSettings();
        gameWorldSettings.create();
        gameRenderer = new GameRenderer(gameCamera, gameWorldSettings,
                Arrays.asList(surfacePhysicModel.getModelInstance(), cylinder.getModelInstance()));

        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(gameCamera);
        Gdx.input.setInputProcessor(input);
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
        cylinder.dispose();
        surfacePhysicModel.dispose();
    }

}