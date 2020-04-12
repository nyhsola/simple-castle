package com.simple.castle.launcher.main.bullet.scene.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.main.GameModels;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.bullet.object.unit.KinematicGameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.render.GameEnvironment;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;
import com.simple.castle.launcher.main.bullet.scene.game.physic.GameScenePhysic;

import java.util.HashMap;
import java.util.Map;

public class GameScene extends ScreenAdapter implements InputProcessor {

    private final Vector3 tempVector = new Vector3();
    private final GameRenderer gameRenderer;
    private final ArrayMap<String, GameObjectConstructor> constructors;
    private final GameScenePhysic gameScenePhysic;
    private final InputMultiplexer inputMultiplexer;
    private final Map<String, AbstractGameObject> sceneGameObjects;

    private GameEnvironment gameEnvironment;
    private GameCamera gameCamera;

    public GameScene(GameRenderer gameRenderer, GameModels gameModels) {
        this.gameRenderer = gameRenderer;
        this.constructors = gameModels.getConstructors();
        this.gameScenePhysic = new GameScenePhysic();
        this.inputMultiplexer = new InputMultiplexer();
        this.sceneGameObjects = new HashMap<>();
    }

    @Override
    public void render(float delta) {
        gameCamera.update();
        gameRenderer.render(gameCamera, sceneGameObjects.values(), gameEnvironment);
        gameScenePhysic.update(gameCamera, Math.min(1f / 30f, delta));
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void show() {
        sceneGameObjects.putAll(Map.ofEntries(
                Map.entry("Surface", new KinematicGameObject(constructors.get("Surface"))),
                Map.entry("Castle-1", new KinematicGameObject(constructors.get("Castle-1"))),
                Map.entry("Castle-2", new KinematicGameObject(constructors.get("Castle-2"))),
                Map.entry("Castle-3", new KinematicGameObject(constructors.get("Castle-3"))),
                Map.entry("Castle-4", new KinematicGameObject(constructors.get("Castle-4"))),
                Map.entry("Spawner-1", new KinematicGameObject(constructors.get("Spawner-1")))));
        sceneGameObjects.forEach((s, gameObject) -> gameScenePhysic.addRigidBody(gameObject));

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        Vector3 redCastlePosition = sceneGameObjects.get("Castle-1").transform.getTranslation(tempVector);
        gameCamera = new GameCamera();
        gameCamera.basePlane = sceneGameObjects.get("Surface");
        gameCamera.position.set(redCastlePosition.x + 10f, redCastlePosition.y + 10f, redCastlePosition.z);
        gameCamera.lookAt(redCastlePosition);

        inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void hide() {
        gameScenePhysic.dispose();
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
}
