package com.simple.castle.launcher.main.bullet.scene.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.main.GameModels;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.bullet.object.unit.KinematicGameObject;
import com.simple.castle.launcher.main.bullet.object.unit.UnitGameObject;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.render.GameEnvironment;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;
import com.simple.castle.launcher.main.bullet.scene.game.physic.GameScenePhysic;
import com.simple.castle.launcher.main.utils.GameIntersectUtils;

import java.util.*;

public class GameScene extends ScreenAdapter implements InputProcessor {

    private final Vector3 tempVector = new Vector3();
    private final Quaternion tempQuaternion = new Quaternion();
    private final BoundingBox tempBoundingBox = new BoundingBox();

    private final BitmapFont bitmapFont;
    private final SpriteBatch batch;

    private final GameRenderer gameRenderer;
    private final ArrayMap<String, GameObjectConstructor> constructors;
    private final GameScenePhysic gameScenePhysic;
    private final InputMultiplexer inputMultiplexer;
    private final Map<String, AbstractGameObject> sceneGameObjects;

    private final List<UnitGameObject> units = new ArrayList<>();

    private GameEnvironment gameEnvironment;
    private GameCamera gameCamera;

    private AbstractGameObject selected;
    private boolean debugDraw = false;

    public GameScene(GameRenderer gameRenderer, GameModels gameModels) {
        this.gameRenderer = gameRenderer;
        this.constructors = gameModels.getConstructors();
        this.gameScenePhysic = new GameScenePhysic();
        this.inputMultiplexer = new InputMultiplexer();
        this.sceneGameObjects = new HashMap<>();
        this.bitmapFont = new BitmapFont();
        this.batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        gameCamera.update(delta);
        this.updateUnit();
        gameRenderer.render(gameCamera, sceneGameObjects.values(), gameEnvironment);
        gameScenePhysic.update(gameCamera, Math.min(1f / 30f, delta), debugDraw);
        this.renderOverlay();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void show() {
        for (int i = 0; i < 123; i++) {
            String format = String.format("Cone.%03d", i);
            sceneGameObjects.put(format, new KinematicGameObject(constructors.get(format)));
        }

        sceneGameObjects.putAll(Map.ofEntries(
                Map.entry("Surface", new KinematicGameObject(constructors.get("Surface"))),
                Map.entry("Castle-1", new KinematicGameObject(constructors.get("Castle-1"))),
                Map.entry("Castle-2", new KinematicGameObject(constructors.get("Castle-2"))),
                Map.entry("Castle-3", new KinematicGameObject(constructors.get("Castle-3"))),
                Map.entry("Castle-4", new KinematicGameObject(constructors.get("Castle-4"))),
                Map.entry("Area-Left-Down", new KinematicGameObject(constructors.get("Area-Left-Down"))),
                Map.entry("Spawner-Red-Left", new KinematicGameObject(constructors.get("Spawner-Red-Left"))),
                Map.entry("Spawner-Blue-Down", new KinematicGameObject(constructors.get("Spawner-Blue-Down")))
        ));
        sceneGameObjects.forEach((s, gameObject) -> gameScenePhysic.addRigidBody(gameObject));

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        Vector3 redCastlePosition = constructors.get("Castle-1").model.getNode("Castle-1").translation;
        gameCamera = new GameCamera();
        gameCamera.basePlane = sceneGameObjects.get("Surface");
        gameCamera.position.set(redCastlePosition.x + 10f, redCastlePosition.y + 10f, redCastlePosition.z);
        gameCamera.lookAt(redCastlePosition);

        inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void hide() {
        bitmapFont.dispose();
        batch.dispose();
        gameScenePhysic.dispose();
        sceneGameObjects.forEach((s, gameObject) -> gameObject.dispose());
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.SPACE == keycode) {
            this.spawnUnit();
        }
        if (Input.Keys.ESCAPE == keycode) {
            debugDraw = !debugDraw;
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
        selected = GameIntersectUtils.intersect(tempBoundingBox, gameCamera, sceneGameObjects.values(), screenX, screenY);
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

    private void spawnUnit() {
        Vector3 spawner1 = sceneGameObjects.get("Spawner-Red-Left").transform.getTranslation(tempVector).cpy();
        Vector3 areaLeftDown = sceneGameObjects.get("Area-Left-Down").transform.getTranslation(tempVector).cpy();

        UnitGameObject unitGameObject = new UnitGameObject(constructors.get("Unit-1"));
        unitGameObject.body.setWorldTransform(new Matrix4());
        unitGameObject.body.translate(spawner1);
        unitGameObject.body.setLinearVelocity(areaLeftDown.sub(spawner1).scl(0.1f));

        gameScenePhysic.addRigidBody(unitGameObject);
        sceneGameObjects.put("Unit-1" + UUID.randomUUID(), unitGameObject);
        units.add(unitGameObject);
    }

    private void updateUnit() {

    }

    private void renderOverlay() {
        batch.begin();
        bitmapFont.draw(batch, "Camera position: " +
                format(gameCamera.position), 0, 20);
        if (selected != null) {
            bitmapFont.draw(batch, "Selected (model): " +
                    "Position: " + format(selected.transform.getTranslation(tempVector)) + " " +
                    "Rotation: " + format(selected.transform.getRotation(tempQuaternion)), 0, 40);

            bitmapFont.draw(batch, "Selected (physic): " +
                    "Position: " + format(selected.body.getWorldTransform().getTranslation(tempVector)) + " " +
                    "Rotation: " + format(selected.body.getWorldTransform().getRotation(tempQuaternion)), 0, 60);

            bitmapFont.draw(batch, selected.node, 0, 80);
        }
        batch.end();
    }

    private String format(Vector3 vector3) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f", vector3.x, vector3.y, vector3.z);
    }

    private String format(Quaternion quaternion) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f, %.2f", quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }
}
