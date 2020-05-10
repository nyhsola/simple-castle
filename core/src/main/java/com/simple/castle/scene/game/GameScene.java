package com.simple.castle.scene.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.simple.castle.core.debug.DebugOverlay;
import com.simple.castle.core.manager.SceneManager;
import com.simple.castle.core.object.constructors.ObjectConstructors;
import com.simple.castle.core.object.constructors.SceneObjectsHandler;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.physic.PhysicEngine;
import com.simple.castle.core.render.BaseCamera;
import com.simple.castle.core.render.BaseEnvironment;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.core.utils.AssetLoader;
import com.simple.castle.core.utils.PropertyLoader;
import com.simple.castle.scene.game.controller.PlayerController;

import java.util.List;

public class GameScene extends ScreenAdapter implements InputProcessor, SceneManager {

    public final static String SCENE_NAME = "game";

    private final BaseRenderer baseRenderer;
    private final PhysicEngine physicEngine;
    private final InputMultiplexer inputMultiplexer;
    private final BaseEnvironment baseEnvironment;
    private final BaseCamera baseCamera;
    private final Model model;
    private final ObjectConstructors objectConstructors;
    private final SceneObjectsHandler sceneObjectsHandler;
    private final PlayerController playerController;
    private final DebugOverlay debugOverlay;
    private boolean debugDraw = false;
    private boolean info = false;

    public GameScene(BaseRenderer baseRenderer) {
        this.debugOverlay = new DebugOverlay();

        this.baseRenderer = baseRenderer;

        this.model = AssetLoader.loadModel();
        this.objectConstructors = new ObjectConstructors.Builder(model)
                .build(PropertyLoader.loadConstructors(SCENE_NAME));
        this.sceneObjectsHandler = new SceneObjectsHandler.Builder(objectConstructors)
                .build(PropertyLoader.loadObjects(SCENE_NAME));
        this.playerController = new PlayerController.Builder(objectConstructors, this)
                .build(PropertyLoader.loadPlayers(SCENE_NAME));
        this.baseCamera = new BaseCamera.Builder(sceneObjectsHandler)
                .build(PropertyLoader.loadProperties(GameScene.SCENE_NAME));

        this.physicEngine = new PhysicEngine(this);
        this.physicEngine.addContactListener(playerController);
        this.sceneObjectsHandler.getSceneObjects().forEach(physicEngine::addRigidBody);

        this.baseEnvironment = new BaseEnvironment();
        this.baseEnvironment.create();

        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(baseCamera);
    }

    @Override
    public void render(float delta) {
        //Camera
        baseCamera.update(delta);

        //Controllers
        playerController.update();

        //Draw and Physic
        baseRenderer.render(baseCamera, sceneObjectsHandler, baseEnvironment);
        physicEngine.update(delta);

        //Debug
        if (debugDraw) {
            physicEngine.debugDraw(baseCamera);
        }
        if (info) {
            debugOverlay.debugInformation.setTimeLeft(playerController.getTimeLeft());
            debugOverlay.debugInformation.setFps(1.0 / delta);
            debugOverlay.debugInformation.setTotalUnits(playerController.getTotalUnits());
            debugOverlay.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        baseCamera.resize(width, height);
        baseCamera.update();

        debugOverlay.resize(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        physicEngine.dispose();
        model.dispose();
        objectConstructors.dispose();
        sceneObjectsHandler.dispose();
        playerController.dispose();
        debugOverlay.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.F1 == keycode) {
            debugDraw = !debugDraw;
        }
        if (Input.Keys.F2 == keycode) {
            info = !info;
        }
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit();
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

    @Override
    public void remove(AbstractGameObject abstractGameObject) {
        physicEngine.removeRigidBody(abstractGameObject);
        sceneObjectsHandler.remove(abstractGameObject);
    }

    @Override
    public void add(AbstractGameObject abstractGameObject) {
        physicEngine.addRigidBody(abstractGameObject);
        sceneObjectsHandler.add(abstractGameObject);
    }

    @Override
    public void addAll(List<? extends AbstractGameObject> abstractGameObjects) {
        abstractGameObjects.forEach(physicEngine::addRigidBody);
        sceneObjectsHandler.addAll(abstractGameObjects);
    }

    @Override
    public AbstractGameObject getByModelName(String name) {
        return sceneObjectsHandler.getByName(name);
    }

    @Override
    public boolean contains(AbstractGameObject abstractGameObject) {
        return sceneObjectsHandler.contains(abstractGameObject);
    }

}
