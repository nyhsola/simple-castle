package com.simple.castle.server.game.scenes;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.simple.castle.server.game.core.debug.DebugInformation;
import com.simple.castle.server.game.core.debug.DebugOverlay;
import com.simple.castle.server.game.core.manager.SceneManager;
import com.simple.castle.server.game.core.object.constructors.ObjectConstructors;
import com.simple.castle.server.game.core.object.constructors.SceneObjectsHandler;
import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.server.game.core.physic.PhysicEngine;
import com.simple.castle.server.game.core.render.BaseCamera;
import com.simple.castle.server.game.core.render.BaseEnvironment;
import com.simple.castle.server.game.core.render.BaseRenderer;
import com.simple.castle.server.game.core.utils.AssetLoader;
import com.simple.castle.server.game.scenes.controller.GameController;
import com.simple.castle.server.game.scenes.controller.PlayerController;

import java.util.List;

public class GameScene extends ScreenAdapter implements InputProcessor, SceneManager {

    private final Model model;
    private final BaseRenderer baseRenderer;
    private final PhysicEngine physicEngine;
    private final InputMultiplexer inputMultiplexer;
    private final BaseEnvironment baseEnvironment;
    private final BaseCamera baseCamera;
    private final ObjectConstructors objectConstructors;
    private final SceneObjectsHandler sceneObjectsHandler;
    private final GameController gameController;
    private final DebugOverlay debugOverlay;
    private boolean debugDraw = false;
    private boolean infoDraw = false;

    public GameScene(BaseRenderer baseRenderer) {
        this.debugOverlay = new DebugOverlay();

        this.baseRenderer = baseRenderer;

        this.model = AssetLoader.loadModel();

        // TODO: 5/14/2020 Re-write on factory
        this.objectConstructors = new ObjectConstructors.Builder(model)
                .build(GameSettings.OBJECT_CONSTRUCTORS_JSON);
        this.sceneObjectsHandler = new SceneObjectsHandler.Builder(objectConstructors)
                .build(GameSettings.SCENE_OBJECTS_JSON);
        this.baseCamera = new BaseCamera.Builder(sceneObjectsHandler)
                .build(GameSettings.CAMERA_BASE_PLANE, GameSettings.CAMERA_INIT_POSITION_FROM);

        this.physicEngine = new PhysicEngine(this);
        this.gameController = new GameController(
                new PlayerController.Builder(objectConstructors, this).build(GameSettings.PLAYERS_JSON));
        this.physicEngine.addContactListener(gameController);

        this.sceneObjectsHandler.getSceneObjects().forEach(abstractGameObject -> physicEngine.addRigidBody(abstractGameObject.body));

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
        gameController.update();

        //Draw and Physic
        baseRenderer.render(baseCamera, sceneObjectsHandler, baseEnvironment);
        physicEngine.update(delta);

        //Debug
        if (debugDraw) {
            physicEngine.debugDraw(baseCamera);
        }
        if (infoDraw) {
            DebugInformation debugInfo = gameController.getDebugInfo();
            debugOverlay.debugInformation.setFps(1.0 / delta);
            debugOverlay.debugInformation.setTimeLeft(debugInfo.getTimeLeft());
            debugOverlay.debugInformation.setTotalUnits(debugInfo.getTotalUnits());
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
        gameController.dispose();
        debugOverlay.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.F1 == keycode) {
            debugDraw = !debugDraw;
        }
        if (Input.Keys.F2 == keycode) {
            infoDraw = !infoDraw;
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
        physicEngine.removeRigidBody(abstractGameObject.body);
        sceneObjectsHandler.remove(abstractGameObject);
    }

    @Override
    public void add(AbstractGameObject abstractGameObject) {
        physicEngine.addRigidBody(abstractGameObject.body);
        sceneObjectsHandler.add(abstractGameObject);
    }

    @Override
    public void addAll(List<? extends AbstractGameObject> abstractGameObjects) {
        abstractGameObjects.forEach(abstractGameObject -> physicEngine.addRigidBody(abstractGameObject.body));
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
