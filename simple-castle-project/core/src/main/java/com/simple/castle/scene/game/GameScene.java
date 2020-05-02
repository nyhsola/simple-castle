package com.simple.castle.scene.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.constructors.SceneObjectsConstructor;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.object.unit.absunit.AbstractGameObject;
import com.simple.castle.render.GameCamera;
import com.simple.castle.render.GameEnvironment;
import com.simple.castle.render.GameRenderer;
import com.simple.castle.scene.game.controller.GameUnitController;
import com.simple.castle.scene.game.physic.GameScenePhysic;
import com.simple.castle.utils.GameIntersectUtils;
import com.simple.castle.utils.ModelLoader;
import com.simple.castle.utils.PropertyLoader;

import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

public class GameScene extends ScreenAdapter implements InputProcessor {

    public final static String SCENE_NAME = "game";

    private final Vector3 tempVector = new Vector3();
    private final Quaternion tempQuaternion = new Quaternion();
    private final BoundingBox tempBoundingBox = new BoundingBox();

    private final BitmapFont bitmapFont;
    private final SpriteBatch batch;

    private final GameRenderer gameRenderer;
    private final GameScenePhysic gameScenePhysic;
    private final SceneObjectsConstructor sceneObjectsConstructor;
    private final InputMultiplexer inputMultiplexer;

    private final GameEnvironment gameEnvironment;
    private final GameCamera gameCamera;

    private AbstractGameObject selected;
    private boolean debugDraw = false;

    private final Model model;
    private final ObjectConstructors objectConstructors;

    private final GameUnitController gameUnitController;

    public GameScene(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
        this.gameScenePhysic = new GameScenePhysic();
        this.inputMultiplexer = new InputMultiplexer();
        this.bitmapFont = new BitmapFont();
        this.batch = new SpriteBatch();

        this.model = ModelLoader.loadModel();
        this.objectConstructors = new ObjectConstructors.Builder(model)
                .build(PropertyLoader.loadConstructorsFromScene(SCENE_NAME));
        this.sceneObjectsConstructor = new SceneObjectsConstructor.Builder(objectConstructors)
                .build(PropertyLoader.loadObjectsFromScene(SCENE_NAME));

        this.gameUnitController = new GameUnitController(sceneObjectsConstructor);
        this.gameScenePhysic.addListenerWithPredicate(gameUnitController);

        this.sceneObjectsConstructor.getSceneObjects().forEach(gameScenePhysic::addRigidBody);

        this.gameEnvironment = new GameEnvironment();
        this.gameEnvironment.create();

        Properties properties = PropertyLoader.loadPropertiesFromScene(GameScene.SCENE_NAME);
        String positionProp = properties.getProperty("camera-init-position-from");
        String basePlaneProp = properties.getProperty("camera-base-plane");
        this.gameCamera = new GameCamera(
                sceneObjectsConstructor.getSceneObject(positionProp).transform.getTranslation(tempVector),
                sceneObjectsConstructor.getSceneObject(basePlaneProp));

        this.inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void render(float delta) {
        gameCamera.update(delta);
        gameUnitController.update();
        gameRenderer.render(gameCamera, sceneObjectsConstructor.getSceneObjects(), gameEnvironment);
        gameScenePhysic.update(gameCamera, Math.min(1f / 30f, delta), debugDraw);
        this.renderOverlay();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        bitmapFont.dispose();
        batch.dispose();
        gameScenePhysic.dispose();
        model.dispose();
        objectConstructors.dispose();
        sceneObjectsConstructor.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.SPACE == keycode) {
            UnitGameObject unitGameObject = gameUnitController.spawnUnit(objectConstructors);
            gameScenePhysic.addRigidBody(unitGameObject);
            sceneObjectsConstructor.addSceneObject("unit-1-" + UUID.randomUUID(), unitGameObject);
        }
        if (Input.Keys.Q == keycode) {
            debugDraw = !debugDraw;
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
        selected = GameIntersectUtils.intersect(tempBoundingBox, gameCamera, sceneObjectsConstructor.getSceneObjects(), screenX, screenY);
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

            bitmapFont.draw(batch, selected.name, 0, 80);
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
