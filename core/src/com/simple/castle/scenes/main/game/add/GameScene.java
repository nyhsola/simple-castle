package com.simple.castle.scenes.main.game.add;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.menu.MenuScene;

import java.util.HashMap;
import java.util.Map;

public class GameScene extends Scene {
    private static final String MODELS_PLANE_G_3_DJ = "models/surface.g3dj";
    public static final String CAMERA_POSITION = "CAMERA_POSITION";

    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private CameraInputController camController;
    private Environment environment;

    public GameScene(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        model = modelLoader.loadModel(Gdx.files.internal(MODELS_PLANE_G_3_DJ));
        instance = new ModelInstance(model);

        camController = new CameraInputController(cam);

        this.setInputProcessor(camController);
    }

    @Override
    public void update() {
        Map<String, Object> map = new HashMap<>();
        map.put(CAMERA_POSITION, new Vector3(cam.position));
        toParent(map);
    }

    @Override
    public void fromParent(Map<String, Object> map) {
        if (map.containsKey(MenuScene.CAMERA_FIELD_OF_VIEW)) {
            cam.fieldOfView = (float) map.get(MenuScene.CAMERA_FIELD_OF_VIEW);
            cam.update();
        }
    }

    @Override
    public void render() {
        camController.update();
        modelBatch.begin(cam);
        modelBatch.render(instance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        model.dispose();
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }
}
