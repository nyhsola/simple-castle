package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.utils.GameIntersectUtils;

public class GameCamera extends ApplicationAdapter implements InputProcessor {
    private static final int FIELD_OF_VIEW = 67;
    private static final float CAMERA_SPEED = 0.5f;

    private PerspectiveCamera perspectiveCamera;
    private CameraInputController camController;

    private final GameObject plane;
    private final Vector3 lookAtInit;

    private boolean keyUpHolds = false;
    private boolean keyDownHolds = false;
    private boolean keyLeftHolds = false;
    private boolean keyRightHolds = false;

    private float previousX;
    private float previousY;
    private boolean mouseDragged = false;

    private Vector3 tempVector = new Vector3();

    public GameCamera(GameObject plane, Vector3 lookAtInit) {
        this.plane = plane;
        this.lookAtInit = lookAtInit;
    }

    @Override
    public void create() {
        perspectiveCamera = new PerspectiveCamera(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        perspectiveCamera.position.set(lookAtInit.x + 10f, lookAtInit.y + 10f, lookAtInit.z);
        perspectiveCamera.lookAt(lookAtInit);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();
        camController = new CameraInputController(perspectiveCamera);
    }

    @Override
    public void resize(int width, int height) {
        perspectiveCamera.viewportWidth = width;
        perspectiveCamera.viewportHeight = height;
        perspectiveCamera.update();
    }

    @Override
    public void render() {
        if (keyUpHolds) {
            perspectiveCamera.position.x = perspectiveCamera.position.x - CAMERA_SPEED;
            perspectiveCamera.update();
        }
        if (keyDownHolds) {
            perspectiveCamera.position.x = perspectiveCamera.position.x + CAMERA_SPEED;
            perspectiveCamera.update();
        }
        if (keyRightHolds) {
            perspectiveCamera.position.z = perspectiveCamera.position.z - CAMERA_SPEED;
            perspectiveCamera.update();
        }
        if (keyLeftHolds) {
            perspectiveCamera.position.z = perspectiveCamera.position.z + CAMERA_SPEED;
            perspectiveCamera.update();
        }

        perspectiveCamera.update();
    }

    public PerspectiveCamera getPerspectiveCamera() {
        return perspectiveCamera;
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
            Vector3 vector3 = GameIntersectUtils.intersectPositionPoint(new BoundingBox(), this, plane, screenX, screenY);
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
            Vector3 vector3 = GameIntersectUtils.intersectPositionPoint(new BoundingBox(), this, plane, screenX, screenY);
            if (vector3 != null) {
                final float deltaX = previousX - vector3.x;
                final float deltaY = previousY - vector3.z;
                perspectiveCamera.position.x = perspectiveCamera.position.x + deltaX;
                perspectiveCamera.position.z = perspectiveCamera.position.z + deltaY;
                perspectiveCamera.update();
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
        Vector3 scl = tempVector.set(perspectiveCamera.direction).scl(amount);
        perspectiveCamera.translate(scl);
        perspectiveCamera.update();
        return false;
    }
}
