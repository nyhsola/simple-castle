package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class BGameCamera extends ApplicationAdapter {
    private PerspectiveCamera cam;
    private CameraInputController camController;

    @Override
    public void create() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 7f, 10f);
        cam.lookAt(0, 4f, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
    }

    public void update() {
        cam.update();
    }

    public InputProcessor getInputProcessor() {
        return camController;
    }

    public PerspectiveCamera getCam() {
        return cam;
    }
}
