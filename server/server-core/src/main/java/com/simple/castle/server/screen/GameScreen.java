package com.simple.castle.server.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.core.ModelSend;
import com.simple.castle.core.asset.AssetLoader;
import com.simple.castle.core.render.BaseCamera;
import com.simple.castle.core.render.BaseEnvironment;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.core.screen.BaseScreen;
import com.simple.castle.server.loader.SceneLoader;
import com.simple.castle.server.manager.SceneManager;
import com.simple.castle.server.physic.world.PhysicWorld;

import java.util.List;

public class GameScreen extends BaseScreen {

    private final Model model;
    private final SceneManager sceneManager;

    private final BaseRenderer baseRenderer;
    private final BaseEnvironment baseEnvironment;
    private final BaseCamera baseCamera;

    private final PhysicWorld physicWorld;

    private boolean isDebug = false;

    public GameScreen(BaseRenderer baseRenderer) {
        this.baseRenderer = baseRenderer;
        this.baseEnvironment = new BaseEnvironment();

        this.model = new AssetLoader().loadModel();
        this.sceneManager = new SceneManager(SceneLoader.loadSceneObjects(model), model);

        ModelInstance groundObject = sceneManager.getObject("ground").getModelInstance();
        Vector3 ground = groundObject.transform.getTranslation(new Vector3());
        this.baseCamera = new BaseCamera(ground, groundObject);

        this.physicWorld = new PhysicWorld();
        this.sceneManager.getAll().forEach(baseObject -> physicWorld.addRigidBody(baseObject.getPhysicObject()));

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

        return super.keyDown(keycode);
    }

    public List<ModelSend> getState() {
        return sceneManager.getSend();
    }
}
