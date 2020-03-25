package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class GameCamera extends ApplicationAdapter implements InputProcessor {
    private static final float CAMERA_SPEED = 0.1f;
    private static final Vector3 tempVector = new Vector3();
    private final ModelInstance surface;
    private final Vector3 position;
    private Camera camera;

    private boolean keyUpHolds = false;
    private boolean keyDownHolds = false;
    private boolean keyLeftHolds = false;
    private boolean keyRightHolds = false;

    private float previousX;
    private float previousY;
    private boolean mouseDragged = false;

    public GameCamera(ModelInstance surface, Vector3 position) {
        this.surface = surface;
        this.position = position;
    }

    @Override
    public void create() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(new Vector3(position.x, position.y + 7f, position.z + 4f));
        camera.lookAt(new Vector3(position.x, position.y, position.z));
        camera.near = 0.1f;
        camera.far = 1000;

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void render() {
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
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
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
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            mouseDragged = true;
            Vector3 vector3 = planeIntersection(screenX, screenY);
            if (vector3 != null) {
                previousX = vector3.x;
                previousY = vector3.z;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseDragged = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (mouseDragged) {
            Vector3 vector3 = planeIntersection(screenX, screenY);
            if (vector3 != null) {
                final float deltaX = previousX - vector3.x;
                final float deltaY = previousY - vector3.z;
                camera.position.x = camera.position.x + deltaX;
                camera.position.z = camera.position.z + deltaY;
                camera.update();
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        Vector3 scl = tempVector.set(camera.direction).scl(amount * 20);
        camera.translate(scl);
        camera.update();
        return false;
    }

    private Vector3 planeIntersection(int screenX, int screenY) {
        Ray pickRay = camera.getPickRay(screenX, screenY);
        Vector3 position = new Vector3();
        surface.transform.getTranslation(position);
        Vector3 intersection = new Vector3();
        if (Intersector.intersectRayBounds(pickRay, surface.calculateBoundingBox(new BoundingBox()), intersection)) {
            return intersection;
        }
        return null;
    }

    public Camera getCamera() {
        return camera;
    }
}
