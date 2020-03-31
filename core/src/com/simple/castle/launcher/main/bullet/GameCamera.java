package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class GameCamera extends ApplicationAdapter {
    private PerspectiveCamera perspectiveCamera;
    private CameraInputController camController;

    @Override
    public void create() {
        perspectiveCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        perspectiveCamera.position.set(3f, 7f, 10f);
        perspectiveCamera.lookAt(0, 4f, 0);
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

    public void update() {
        perspectiveCamera.update();
    }

    public InputProcessor getInputProcessor() {
        return camController;
    }

    public PerspectiveCamera getPerspectiveCamera() {
        return perspectiveCamera;
    }
}
