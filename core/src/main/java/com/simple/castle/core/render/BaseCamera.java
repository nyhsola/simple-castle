package com.simple.castle.core.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.simple.castle.core.object.constructors.SceneObjectsHandler;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.utils.GameIntersectUtils;

import java.util.Properties;

public class BaseCamera extends PerspectiveCamera implements InputProcessor {

    public com.simple.castle.core.object.unit.abs.AbstractGameObject basePlane;

    private static final int FIELD_OF_VIEW = 67;
    private static final float CAMERA_SPEED = 25;
    private static final float NEAR = 1f;
    private static final float FAR = 300f;

    private boolean mouseDragged = false;

    private boolean keyUpHolds = false;
    private boolean keyDownHolds = false;
    private boolean keyLeftHolds = false;
    private boolean keyRightHolds = false;

    private boolean keyCtrlHolds = false;

    private float previousX;
    private float previousY;

    private final Vector3 tempVector = new Vector3();

    private BaseCamera(Vector3 startObjectPosition, AbstractGameObject ground) {
        super(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.basePlane = ground;
        this.position.set(startObjectPosition.x + 10f, startObjectPosition.y + 10f, startObjectPosition.z);
        this.lookAt(startObjectPosition);

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
        if (Input.Keys.CONTROL_LEFT == keycode) {
            keyCtrlHolds = true;
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
        if (Input.Keys.CONTROL_LEFT == keycode) {
            keyCtrlHolds = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (basePlane != null && button == Input.Buttons.LEFT) {
            mouseDragged = true;
            Vector3 vector3 = GameIntersectUtils.intersectPositionPoint(new BoundingBox(), this, basePlane, screenX, screenY);
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
        if (basePlane != null && mouseDragged) {
            Vector3 vector3 = com.simple.castle.core.utils.GameIntersectUtils.intersectPositionPoint(new BoundingBox(), this, basePlane, screenX, screenY);
            if (vector3 != null) {
                final float deltaX = previousX - vector3.x;
                final float deltaY = previousY - vector3.z;
                position.x += deltaX;
                position.z += deltaY;
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
        if (!keyCtrlHolds) {
            translate(tempVector.set(direction).scl(amount));
        }
        // TODO: 4/16/2020 By holding ctrl, change angle between camera and surface
        return false;
    }

    public static final class Builder {
        private final com.simple.castle.core.object.constructors.SceneObjectsHandler sceneObjectsHandler;

        public Builder(SceneObjectsHandler sceneObjectsHandler) {
            this.sceneObjectsHandler = sceneObjectsHandler;
        }

        public BaseCamera build(Properties sceneProperties) {
            String positionProp = sceneProperties.getProperty("camera-init-position-from");
            String basePlaneProp = sceneProperties.getProperty("camera-base-plane");

            return new BaseCamera(
                    sceneObjectsHandler.getByName(positionProp).transform.getTranslation(new Vector3()),
                    sceneObjectsHandler.getByName(basePlaneProp));
        }
    }
}
