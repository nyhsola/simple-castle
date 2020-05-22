package com.simple.castle.base.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class BaseCamera extends PerspectiveCamera implements InputProcessor {

    private static final int FIELD_OF_VIEW = 67;
    private static final float CAMERA_SPEED = 25;
    private static final float NEAR = 1f;
    private static final float FAR = 300f;

    private final Vector3 tempVector = new Vector3();

    private boolean keyUpHolds = false;
    private boolean keyDownHolds = false;
    private boolean keyLeftHolds = false;
    private boolean keyRightHolds = false;

    public BaseCamera(Vector3 startPosition) {
        super(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.position.set(startPosition.x + 10f, startPosition.y + 10f, startPosition.z);
        this.lookAt(startPosition);

        this.near = NEAR;
        this.far = FAR;
    }

    public void resize(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;
    }

    public void update(float delta) {
        float cameraSpeed = CAMERA_SPEED * delta;
        if (keyUpHolds) {
            position.x = position.x - cameraSpeed;
        }
        if (keyDownHolds) {
            position.x = position.x + cameraSpeed;
        }
        if (keyRightHolds) {
            position.z = position.z - cameraSpeed;
        }
        if (keyLeftHolds) {
            position.z = position.z + cameraSpeed;
        }
        super.update();
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        translate(tempVector.set(direction).scl(amount));
        return false;
    }

}