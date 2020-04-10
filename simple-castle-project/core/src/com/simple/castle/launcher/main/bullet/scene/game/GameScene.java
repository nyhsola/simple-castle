package com.simple.castle.launcher.main.bullet.scene.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.controller.GameSelectItemController;
import com.simple.castle.launcher.main.bullet.controller.GameUnitSpawner;
import com.simple.castle.launcher.main.bullet.main.GameModels;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.bullet.object.unit.KinematicGameObject;
import com.simple.castle.launcher.main.bullet.object.unit.UnitGameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.render.GameEnvironment;
import com.simple.castle.launcher.main.bullet.render.GameOverlay;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;
import com.simple.castle.launcher.main.bullet.scene.game.physic.GameSceneWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameScene extends ScreenAdapter implements InputProcessor {

    private final Vector3 tempVector = new Vector3();

    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private final GameRenderer gameRenderer;
    private final GameModels gameModels;
    private final GameSceneWorld gameSceneWorld;

    private Map<String, AbstractGameObject> sceneGameObjects;

    private GameOverlay gameOverlay;

    private GameEnvironment gameEnvironment;
    private GameCamera gameCamera;

    private GameUnitSpawner gameUnitSpawner;
    private GameSelectItemController gameSelectItemController;

    public GameScene(GameRenderer gameRenderer, GameModels gameModels) {
        this.gameRenderer = gameRenderer;
        this.gameModels = gameModels;
        this.gameSceneWorld = new GameSceneWorld();
    }

    @Override
    public void render(float delta) {
        gameCamera.update();
        gameRenderer.render(gameCamera, sceneGameObjects.values(), gameEnvironment);
        gameSceneWorld.update(gameCamera, Math.min(1f / 30f, delta));
        gameOverlay.render(gameCamera, gameSelectItemController.getSelectedObject());
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void show() {
        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        gameOverlay = new GameOverlay();
        gameOverlay.create();

        ArrayMap<String, GameObjectConstructor> constructors = gameModels.getConstructors();

        sceneGameObjects = new HashMap<>(Map.ofEntries(
                Map.entry("Surface", new KinematicGameObject(constructors.get("Surface"))),
                Map.entry("Castle-1", new KinematicGameObject(constructors.get("Castle-1"))),
                Map.entry("Castle-2", new KinematicGameObject(constructors.get("Castle-2"))),
                Map.entry("Castle-3", new KinematicGameObject(constructors.get("Castle-3"))),
                Map.entry("Castle-4", new KinematicGameObject(constructors.get("Castle-4"))),
                Map.entry("Spawner-1", new KinematicGameObject(constructors.get("Spawner-1")))));

        sceneGameObjects.forEach((s, gameObject) -> gameSceneWorld.addRigidBody(gameObject));

        Vector3 redCastlePosition = sceneGameObjects.get("Castle-1").transform.getTranslation(tempVector);

        gameCamera = new GameCamera();
        gameCamera.basePlane = sceneGameObjects.get("Surface");
        gameCamera.position.set(redCastlePosition.x + 10f, redCastlePosition.y + 10f, redCastlePosition.z);
        gameCamera.lookAt(redCastlePosition);

        gameUnitSpawner = new GameUnitSpawner(this);
        gameSelectItemController = new GameSelectItemController(gameCamera, this);

        inputMultiplexer.addProcessor(gameSelectItemController);
        inputMultiplexer.addProcessor(gameUnitSpawner);
        inputMultiplexer.addProcessor(gameSceneWorld);
        inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void hide() {
        gameSceneWorld.dispose();
        gameOverlay.dispose();
        sceneGameObjects.forEach((s, gameObject) -> gameObject.dispose());
    }

    @Override
    public boolean keyDown(int keycode) {
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

    public void spawn() {
        UnitGameObject unitGameObject = new UnitGameObject(gameModels.getConstructors().get("Unit-1"));

        sceneGameObjects.put(UUID.randomUUID().toString(), unitGameObject);

        unitGameObject.body.setWorldTransform(new Matrix4());
        unitGameObject.body.translate(sceneGameObjects.get("Spawner-1").transform.getTranslation(new Vector3()));

        gameSceneWorld.addRigidBody(unitGameObject);
    }

    public Map<String, AbstractGameObject> getSceneGameObjects() {
        return sceneGameObjects;
    }
}
