package com.simple.castle.scene.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.simple.castle.object.absunit.AbstractGameObject;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.render.GameCamera;
import com.simple.castle.render.GameEnvironment;
import com.simple.castle.render.GameRenderer;
import com.simple.castle.scene.game.object.GameModelsConstructor;
import com.simple.castle.scene.game.object.GameSceneObjects;
import com.simple.castle.scene.game.physic.GameScenePhysic;
import com.simple.castle.utils.GameIntersectUtils;
import com.simple.castle.utils.ModelLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GameScene extends ScreenAdapter implements InputProcessor {

    private final Vector3 tempVector = new Vector3();
    private final Quaternion tempQuaternion = new Quaternion();
    private final BoundingBox tempBoundingBox = new BoundingBox();

    private final BitmapFont bitmapFont;
    private final SpriteBatch batch;

    private final GameRenderer gameRenderer;
    private final GameScenePhysic gameScenePhysic;
    private final GameSceneObjects gameSceneObjects;
    private final InputMultiplexer inputMultiplexer;

    private final List<UnitGameObject> units = new ArrayList<>();

    private GameEnvironment gameEnvironment;
    private GameCamera gameCamera;

    private AbstractGameObject selected;
    private boolean debugDraw = false;

    private final Model model;
    private final GameModelsConstructor gameModelsConstructor;

    public GameScene(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
        this.gameScenePhysic = new GameScenePhysic();
        this.inputMultiplexer = new InputMultiplexer();
        this.bitmapFont = new BitmapFont();
        this.batch = new SpriteBatch();

        this.model = ModelLoader.loadModel();
        this.gameModelsConstructor = new GameModelsConstructor(model);
        this.gameSceneObjects = new GameSceneObjects(gameModelsConstructor);


    }

    @Override
    public void render(float delta) {
        gameCamera.update(delta);
        this.updateUnit();
        gameRenderer.render(gameCamera, gameSceneObjects.getSceneObjects(), gameEnvironment);
        gameScenePhysic.update(gameCamera, Math.min(1f / 30f, delta), debugDraw);
        this.renderOverlay();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void show() {
        gameSceneObjects.getSceneObjects().forEach(gameScenePhysic::addRigidBody);

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        Vector3 redCastlePosition = gameSceneObjects.getSceneObject("Castle-1").transform.getTranslation(tempVector);

        gameCamera = new GameCamera();
        gameCamera.basePlane = gameSceneObjects.getSceneObject("Surface");
        gameCamera.position.set(redCastlePosition.x + 10f, redCastlePosition.y + 10f, redCastlePosition.z);
        gameCamera.lookAt(redCastlePosition);

        inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void hide() {
        bitmapFont.dispose();
        batch.dispose();
        gameScenePhysic.dispose();
        model.dispose();
        gameModelsConstructor.dispose();
        gameSceneObjects.dispose();
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
        selected = GameIntersectUtils.intersect(tempBoundingBox, gameCamera, gameSceneObjects.getSceneObjects(), screenX, screenY);
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
        Vector3 spawnerRedLeft = gameSceneObjects.getSceneObject("Spawner-Red-Left").transform
                .getTranslation(tempVector).cpy();
        Vector3 areaLeftDown = gameSceneObjects.getSceneObject("Area-Left-Down").transform
                .getTranslation(tempVector).cpy();

        UnitGameObject unitGameObject = new UnitGameObject(gameModelsConstructor.getConstructor("Unit-1"));

        unitGameObject.body.setWorldTransform(new Matrix4());
        unitGameObject.body.translate(spawnerRedLeft);
        unitGameObject.body.setLinearVelocity(areaLeftDown.sub(spawnerRedLeft).scl(0.1f));

        gameScenePhysic.addRigidBody(unitGameObject);
        gameSceneObjects.addSceneObject("Unit-1-" + UUID.randomUUID(), unitGameObject);

        units.add(unitGameObject);
    }

    private void updateUnit() {

    }

    private void renderOverlay() {
        batch.begin();
        bitmapFont.draw(batch, "Camera position: " +
                format(gameCamera.position), 0, 20);
        if (selected != null) {
            // TODO: 4/16/2020 Correctly resize text
            bitmapFont.draw(batch, "Selected (model): " +
                    "Position: " + format(selected.transform.getTranslation(tempVector)) + " " +
                    "Rotation: " + format(selected.transform.getRotation(tempQuaternion)), 0, 40);

            bitmapFont.draw(batch, "Selected (physic): " +
                    "Position: " + format(selected.body.getWorldTransform().getTranslation(tempVector)) + " " +
                    "Rotation: " + format(selected.body.getWorldTransform().getRotation(tempQuaternion)), 0, 60);

            bitmapFont.draw(batch, (String) selected.body.userData, 0, 80);
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
