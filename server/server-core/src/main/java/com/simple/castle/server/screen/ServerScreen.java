package com.simple.castle.server.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.core.ModelSend;
import com.simple.castle.core.asset.AssetLoader;
import com.simple.castle.core.render.BaseCamera;
import com.simple.castle.core.render.BaseEnvironment;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.server.loader.SceneLoader;
import com.simple.castle.server.manager.SceneManager;
import com.simple.castle.server.physic.world.PhysicWorld;

import java.util.List;

public class ServerScreen extends ScreenAdapter implements InputProcessor {

    private final BaseRenderer baseRenderer;
    private final BaseEnvironment baseEnvironment;
    private final BaseCamera baseCamera;

    private final InputMultiplexer inputMultiplexer;

    private final PhysicWorld physicWorld;

    private final Model model;
    private final SceneManager sceneManager;

    private boolean isDebug = false;

    public ServerScreen(BaseRenderer baseRenderer) {
        this.baseRenderer = baseRenderer;

        this.model = new AssetLoader().loadModel();
        this.sceneManager = new SceneManager(new SceneLoader().loadSceneObjects(model), model);
        this.baseEnvironment = new BaseEnvironment();

        ModelInstance groundObject = sceneManager.getObject("ground").getModelInstance();
        Vector3 ground = groundObject.transform.getTranslation(new Vector3());
        this.baseCamera = new BaseCamera(ground, groundObject);

        this.physicWorld = new PhysicWorld();
        this.sceneManager.getAll().forEach(baseObject -> physicWorld.addRigidBody(baseObject.getPhysicObject()));

        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(baseCamera);
    }

    @Override
    public void render(float delta) {
        baseCamera.update(delta);
        physicWorld.update(delta);
        baseRenderer.render(baseCamera, sceneManager.getDrawables(), baseEnvironment);

        if (isDebug) {
            physicWorld.debugDraw(baseCamera);
        }

    }

    @Override
    public void resize(int width, int height) {
        baseCamera.resize(width, height);
        baseCamera.update();
    }

    @Override
    public void dispose() {
        baseRenderer.dispose();
        physicWorld.dispose();
        sceneManager.dispose();
        model.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit();
        }

        if (Input.Keys.F1 == keycode) {
            isDebug = !isDebug;
        }

        return inputMultiplexer.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputMultiplexer.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputMultiplexer.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return inputMultiplexer.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return inputMultiplexer.scrolled(amount);
    }

    public List<ModelSend> getState() {
        return sceneManager.getSend();
    }
}
