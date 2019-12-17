package com.simple.castle.scenes.main.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.UBJsonReader;
import com.simple.castle.constants.Settings;
import com.simple.castle.scene.Scene;

public class GameScene extends Scene {

    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private Model model1;
    private ModelInstance instance;
    private ModelInstance instance1;
    private CameraInputController camController;
    private Environment environment;
    private AssetManager assetManager;

    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        UBJsonReader ubJsonReader = new UBJsonReader();
        G3dModelLoader g3dModelLoader = new G3dModelLoader(ubJsonReader);

        Gdx.app.log("", Gdx.files.internal("models/plane.g3dj").exists() ? "exists" : "does not exist");

        FileHandle internal = Gdx.files.internal("models/plane.g3dj");
        System.out.print(internal.file().getAbsolutePath());
        model1 = g3dModelLoader.loadModel(internal);
        instance1 = new ModelInstance(model1);

        this.setInputProcessor(camController);
    }

    @Override
    public void settingUpdated(String name, String value) {
        if (Settings.FIELD_OF_VIEW.equals(name)) {
            cam.fieldOfView = value == null ? 0.0f : Float.parseFloat(value);
            cam.update();
        }
    }

    @Override
    public void render() {
        camController.update();

        modelBatch.begin(cam);
//        modelBatch.render(instance, environment);
        modelBatch.render(instance1, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        model.dispose();
    }

}
