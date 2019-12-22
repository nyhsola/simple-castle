package com.simple.castle.scenes.main.game;

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
import com.badlogic.gdx.utils.JsonReader;
import com.simple.castle.constants.Settings;
import com.simple.castle.scene.Scene;

public class GameScene extends Scene {

    private static final String MODELS_PLANE_G_3_DJ = "models/surface.g3dj";

    private G3dModelLoader modelLoader;
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private CameraInputController camController;
    private Environment environment;

    public GameScene() {
    }

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

        modelLoader = new G3dModelLoader(new JsonReader());
        model = modelLoader.loadModel(Gdx.files.internal(MODELS_PLANE_G_3_DJ));
        instance = new ModelInstance(model);
    }


    @Override
    public boolean keyDown(int keycode) {
        return camController.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return camController.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return camController.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return camController.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return camController.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return camController.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return camController.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return camController.scrolled(amount);
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
        modelBatch.render(instance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        model.dispose();
    }

}
