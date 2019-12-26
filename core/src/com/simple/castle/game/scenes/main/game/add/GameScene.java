package com.simple.castle.game.scenes.main.game.add;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.game.add.objects.Camera;

import java.util.Map;

public class GameScene extends Scene {
    private static final String MODELS_PLANE_G_3_DJ = "models/surface.g3dj";

    private Camera camera;

    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
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

        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        model = modelLoader.loadModel(Gdx.files.internal(MODELS_PLANE_G_3_DJ));
        instance = new ModelInstance(model);

        Node node = instance.getNode("RedCube");
        Vector3 position = node.translation;

        camera = new Camera(this,
                new Vector3(position.x, position.y + 7f, position.z + 4f),
                new Vector3(position.x, position.y, position.z),
                67, 1, 300);

        manager
                .addScene(Scenes.CAMERA, camera)
                .setCurrentScene(Scenes.CAMERA);
    }

    @Override
    public void render() {
        modelBatch.begin(camera.getCamera());
        modelBatch.render(instance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        model.dispose();
        modelBatch.dispose();
    }

    @Override
    public void fromParent(Map<String, Object> map) {
        toChild(map);
    }

    @Override
    public void fromChild(Map<String, Object> map) {
        toParent(map);
    }

}
