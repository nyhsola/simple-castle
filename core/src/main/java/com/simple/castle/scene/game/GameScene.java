package com.simple.castle.scene.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.simple.castle.debug.DebugOverlay;
import com.simple.castle.listener.SceneObjectManager;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.constructors.SceneObjectsHandler;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.physic.PhysicEngine;
import com.simple.castle.render.GameCamera;
import com.simple.castle.render.GameEnvironment;
import com.simple.castle.render.GameRenderer;
import com.simple.castle.scene.game.controller.PlayerController;
import com.simple.castle.utils.AssetLoader;
import com.simple.castle.utils.PropertyLoader;

import java.util.List;

public class GameScene extends ScreenAdapter implements InputProcessor, SceneObjectManager {

    public final static String SCENE_NAME = "game";

    private final GameRenderer gameRenderer;
    private final PhysicEngine physicEngine;
    private final InputMultiplexer inputMultiplexer;
    private final GameEnvironment gameEnvironment;
    private final GameCamera gameCamera;
    private final Model model;
    private final ObjectConstructors objectConstructors;
    private final SceneObjectsHandler sceneObjectsHandler;
    private final PlayerController playerController;
    private final com.simple.castle.debug.DebugOverlay debugOverlay;
    private boolean debugDraw = false;
    private boolean info = false;

    public GameScene(GameRenderer gameRenderer) {
        this.debugOverlay = new DebugOverlay();

        this.gameRenderer = gameRenderer;
        this.physicEngine = new PhysicEngine();

        this.model = AssetLoader.loadModel();
        this.objectConstructors = new ObjectConstructors.Builder(model)
                .build(PropertyLoader.loadConstructors(SCENE_NAME));
        this.sceneObjectsHandler = new SceneObjectsHandler.Builder(objectConstructors)
                .build(PropertyLoader.loadObjects(SCENE_NAME));
        this.playerController = new PlayerController.Builder(objectConstructors, this)
                .build(PropertyLoader.loadPlayers(SCENE_NAME));
        this.gameCamera = new GameCamera.Builder(sceneObjectsHandler)
                .build(PropertyLoader.loadProperties(GameScene.SCENE_NAME));

        this.physicEngine.addContactListener(playerController);
        this.sceneObjectsHandler.getSceneObjects().forEach(physicEngine::addRigidBody);

        this.gameEnvironment = new GameEnvironment();
        this.gameEnvironment.create();

        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void render(float delta) {
        //Camera
        gameCamera.update(delta);

        //Controllers
        playerController.update();

        //Draw and Physic
        gameRenderer.render(gameCamera, sceneObjectsHandler, gameEnvironment);
        physicEngine.update(gameCamera, delta);

        //Debug
        if (debugDraw) {
            physicEngine.debugDraw(gameCamera);
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
        gameCamera.resize(width, height);
        gameCamera.update();

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
