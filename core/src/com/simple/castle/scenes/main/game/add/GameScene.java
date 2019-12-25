package com.simple.castle.scenes.main.game.add;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
import com.simple.castle.scene.Scene;
import com.simple.castle.scenes.main.menu.MenuScene;

import java.util.HashMap;
import java.util.Map;

public class GameScene extends Scene {
    private static final String MODELS_PLANE_G_3_DJ = "models/surface.g3dj";
    public static final String CAMERA_POSITION = "CAMERA_POSITION";
    public static final float CAMERA_SPEED = 0.1f;

    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private Environment environment;

    private final Vector3 tempVector1 = new Vector3();

    private boolean keyUpHolds = false;
    private boolean keyDownHolds = false;
    private boolean keyLeftHolds = false;
    private boolean keyRightHolds = false;

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

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(position.x,
                position.y + 7f,
                position.z + 4f);
        cam.lookAt(position.x, position.y, position.z);

        cam.near = 1f;
        cam.far = 300f;
        cam.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(Input.Keys.UP == keycode || Input.Keys.W == keycode) {
            keyUpHolds = true;
        }
        if(Input.Keys.DOWN == keycode || Input.Keys.S == keycode) {
            keyDownHolds = true;
        }
        if(Input.Keys.LEFT == keycode || Input.Keys.A == keycode) {
            keyLeftHolds = true;
        }
        if(Input.Keys.RIGHT == keycode || Input.Keys.D == keycode) {
            keyRightHolds = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(Input.Keys.UP == keycode || Input.Keys.W == keycode) {
            keyUpHolds = false;
        }
        if(Input.Keys.DOWN == keycode || Input.Keys.S == keycode) {
            keyDownHolds = false;
        }
        if(Input.Keys.LEFT == keycode || Input.Keys.A == keycode) {
            keyLeftHolds = false;
        }
        if(Input.Keys.RIGHT == keycode || Input.Keys.D == keycode) {
            keyRightHolds = false;
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        Vector3 scl = tempVector1.set(cam.direction).scl(amount);
        if(scl.y > 3 || scl.y < 70) {
            cam.translate(scl);
            cam.update();
        }
        return false;
    }

    @Override
    public void update() {
        Map<String, Object> map = new HashMap<>();
        map.put(CAMERA_POSITION, new Vector3(cam.position));
        toParent(map);

        if(keyUpHolds) {
            cam.position.z = cam.position.z - CAMERA_SPEED;
            cam.update();
        }
        if(keyDownHolds) {
            cam.position.z = cam.position.z + CAMERA_SPEED;
            cam.update();
        }
        if(keyRightHolds) {
            cam.position.x = cam.position.x + CAMERA_SPEED;
            cam.update();
        }
        if(keyLeftHolds) {
            cam.position.x = cam.position.x - CAMERA_SPEED;
            cam.update();
        }
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
