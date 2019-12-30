package com.simple.castle.game.scenes.main.game.add.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.menu.MenuScene;

import java.util.HashMap;
import java.util.Map;

public class Camera extends Scene {

    public static final float CAMERA_SPEED = 0.1f;
    public static final String CAMERA_POSITION = "CAMERA_POSITION";

    private final PerspectiveCamera camera;

    private final Vector3 tempVector1 = new Vector3();

    private boolean keyUpHolds = false;

    private boolean keyDownHolds = false;
    private boolean keyLeftHolds = false;
    private boolean keyRightHolds = false;

    public Camera(Scene parent, Vector3 position, Vector3 lookAt, float fieldOfView, float near, float far) {
        super(parent);

        camera = new PerspectiveCamera(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(position);
        camera.lookAt(lookAt);

        camera.near = near;
        camera.far = far;
    }

    @Override
    public void create() {
        camera.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.UP == keycode || Input.Keys.W == keycode) {
            keyUpHolds = true;
        }
        if (Input.Keys.DOWN == keycode || Input.Keys.S == keycode) {
            keyDownHolds = true;
        }
        if (Input.Keys.LEFT == keycode || Input.Keys.A == keycode) {
            keyLeftHolds = true;
        }
        if (Input.Keys.RIGHT == keycode || Input.Keys.D == keycode) {
            keyRightHolds = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (Input.Keys.UP == keycode || Input.Keys.W == keycode) {
            keyUpHolds = false;
        }
        if (Input.Keys.DOWN == keycode || Input.Keys.S == keycode) {
            keyDownHolds = false;
        }
        if (Input.Keys.LEFT == keycode || Input.Keys.A == keycode) {
            keyLeftHolds = false;
        }
        if (Input.Keys.RIGHT == keycode || Input.Keys.D == keycode) {
            keyRightHolds = false;
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        Vector3 scl = tempVector1.set(camera.direction).scl(amount);
        camera.translate(scl);
        camera.update();
        return false;
    }

    @Override
    public void update(float delta) {
        Map<String, Object> map = new HashMap<>();
        map.put(CAMERA_POSITION, new Vector3(camera.position));
        notifyParent(map);

        if (keyUpHolds) {
            camera.position.z = camera.position.z - CAMERA_SPEED;
            camera.update();
        }
        if (keyDownHolds) {
            camera.position.z = camera.position.z + CAMERA_SPEED;
            camera.update();
        }
        if (keyRightHolds) {
            camera.position.x = camera.position.x + CAMERA_SPEED;
            camera.update();
        }
        if (keyLeftHolds) {
            camera.position.x = camera.position.x - CAMERA_SPEED;
            camera.update();
        }
    }

    @Override
    public void onParentEvent(Map<String, Object> map) {
        if (map.containsKey(MenuScene.CAMERA_FIELD_OF_VIEW)) {
            camera.fieldOfView = (float) map.get(MenuScene.CAMERA_FIELD_OF_VIEW);
            camera.update();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

}
