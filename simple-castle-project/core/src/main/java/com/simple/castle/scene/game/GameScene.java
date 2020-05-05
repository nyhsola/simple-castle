package com.simple.castle.scene.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
import com.simple.castle.utils.GameIntersectUtils;
import com.simple.castle.utils.PropertyLoader;

import java.util.Locale;

public class GameScene extends ScreenAdapter implements InputProcessor, SceneObjectManager {

    public final static String SCENE_NAME = "game";

    private final Vector3 tempVector = new Vector3();
    private final Quaternion tempQuaternion = new Quaternion();
    private final BoundingBox tempBoundingBox = new BoundingBox();

    private final BitmapFont bitmapFont;
    private final SpriteBatch batch;

    private final GameRenderer gameRenderer;
    private final PhysicEngine physicEngine;
    private final InputMultiplexer inputMultiplexer;

    private final GameEnvironment gameEnvironment;
    private final GameCamera gameCamera;

    private final Model model;
    private final ObjectConstructors objectConstructors;
    private final SceneObjectsHandler sceneObjectsHandler;
    private final Stage stage;
    private final Skin skin;

    private final PlayerController playerController;
    private final TextButton timeButton;
    private AbstractGameObject selected;
    private boolean debugDraw = false;

    public GameScene(GameRenderer gameRenderer) {
        skin = AssetLoader.loadSkin();

        Label timeLabel = new Label("Spawn in: ", skin);
        timeButton = new TextButton(Long.toString(PlayerController.spawnEvery), skin);
        Table table = new Table();
        table.align(Align.bottomRight).add(timeLabel, timeButton);
        table.setFillParent(true);

        stage = new Stage(new ScreenViewport());
        stage.addActor(table);

        this.gameRenderer = gameRenderer;
        this.physicEngine = new PhysicEngine();
        this.bitmapFont = new BitmapFont();
        this.batch = new SpriteBatch();

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
        this.inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //Camera
        gameCamera.update(delta);

        //Controllers
        playerController.update();

        //Draw and Physic
        gameRenderer.render(gameCamera, sceneObjectsHandler.getSceneObjects(), gameEnvironment);
        physicEngine.update(gameCamera, Math.min(1f / 30f, delta), debugDraw);

        //Overlay
        timeButton.setText(Long.toString(playerController.getTimeLeft() / 1000));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
        gameCamera.update();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        bitmapFont.dispose();
        batch.dispose();
        physicEngine.dispose();
        model.dispose();
        objectConstructors.dispose();
        sceneObjectsHandler.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
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
        selected = GameIntersectUtils.intersect(tempBoundingBox, gameCamera, sceneObjectsHandler.getSceneObjects(), screenX, screenY);
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

            bitmapFont.draw(batch, String.valueOf(selected.body.userData), 0, 80);
        }
        batch.end();
    }

    private String format(Vector3 vector3) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f", vector3.x, vector3.y, vector3.z);
    }

    private String format(Quaternion quaternion) {
        return String.format(Locale.getDefault(), "%.2f, %.2f, %.2f, %.2f", quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }

    @Override
    public void remove(AbstractGameObject abstractGameObject) {
        physicEngine.removeRigidBody(abstractGameObject);
        sceneObjectsHandler.disposeObject(abstractGameObject);
    }

    @Override
    public void add(AbstractGameObject abstractGameObject) {
        physicEngine.addRigidBody(abstractGameObject);
        sceneObjectsHandler.addSceneObject(abstractGameObject);
    }

    @Override
    public AbstractGameObject getByUserData(String userData) {
        return sceneObjectsHandler.getSceneObjectByUserData(userData);
    }

    @Override
    public AbstractGameObject getByName(String name) {
        return sceneObjectsHandler.getSceneObjectByModelName(name);
    }

    @Override
    public boolean contains(AbstractGameObject abstractGameObject) {
        return sceneObjectsHandler.contains(abstractGameObject);
    }

    @Override
    public boolean contains(String userData) {
        return sceneObjectsHandler.contains(userData);
    }
}
