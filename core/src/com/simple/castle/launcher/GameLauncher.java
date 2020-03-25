package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLauncher extends ApplicationAdapter {
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private ModelBatch modelBatch;
    private Model model;

    private btCollisionObject surfaceObject;
    private btCollisionObject sphereObject;

    private GameCamera gameCamera;
    private WorldSettings worldSettings;
    private List<ModelInstance> modelInstances;

    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;

    private void preInit() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);

        modelBatch = new ModelBatch();
        model = ModelLoader.loadModel();
    }

    private void postInit() {
        ModelInstance surface = new ModelInstance(model, "Surface");
        ModelInstance redCube = new ModelInstance(model, "RedCube");
        ModelInstance sphere = new ModelInstance(model, "Sphere");

        BoundingBox surfaceBox = new BoundingBox();
        BoundingBox sphereBox = new BoundingBox();

        surface.calculateBoundingBox(surfaceBox);
        sphere.calculateBoundingBox(sphereBox);

        surfaceObject = new btCollisionObject();
        surfaceObject.setCollisionShape(new btBoxShape(surfaceBox.getDimensions(new Vector3())));
        surfaceObject.setWorldTransform(surface.transform);

        sphereObject = new btCollisionObject();
        sphereObject.setCollisionShape(new btBoxShape(sphereBox.getDimensions(new Vector3())));
        sphereObject.setWorldTransform(sphere.transform);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        BoxShapeBuilder.build(modelBuilder.part("id", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material()), surfaceBox);
        ModelInstance surfaceBoxModel = new ModelInstance(modelBuilder.end());

        modelBuilder.begin();
        BoxShapeBuilder.build(modelBuilder.part("id", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material()), sphereBox);
        ModelInstance sphereBoxModel = new ModelInstance(modelBuilder.end());

        gameCamera = new GameCamera(surface, redCube.getNode("RedCube").translation);
        gameCamera.create();

        worldSettings = new WorldSettings();
        worldSettings.create();

        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(gameCamera);
        Gdx.input.setInputProcessor(input);

        modelInstances = new ArrayList<>();
        modelInstances.addAll(Arrays.asList(surface, redCube, sphere, sphereBoxModel, surfaceBoxModel));
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
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
        surfaceObject.dispose();
        sphereObject.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();
    }

}
