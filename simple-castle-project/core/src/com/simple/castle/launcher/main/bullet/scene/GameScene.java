package com.simple.castle.launcher.main.bullet.scene;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.launcher.main.bullet.controller.GameSelectItemController;
import com.simple.castle.launcher.main.bullet.controller.GameUnitSpawner;
import com.simple.castle.launcher.main.bullet.main.ModelFactory;
import com.simple.castle.launcher.main.bullet.object.unit.SphereUnit;
import com.simple.castle.launcher.main.bullet.object.unit.controller.MainUnitController;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.render.GameEnvironment;
import com.simple.castle.launcher.main.bullet.render.GameOverlay;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;

public class GameScene implements ApplicationListener, InputProcessor {

    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private GamePhysicWorld gamePhysicWorld;
    private GameEnvironment gameEnvironment;
    private GameRenderer gameRenderer;
    private GameCamera gameCamera;
    private ModelFactory modelFactory;
    private GameOverlay gameOverlay;

    private GameUnitSpawner gameUnitSpawner;
    private MainUnitController mainUnitController;

    private GameSelectItemController gameSelectItemController;

    @Override
    public void create() {
        gameRenderer = new GameRenderer();
        gameRenderer.create();

        gamePhysicWorld = new GamePhysicWorld();
        gamePhysicWorld.create();

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        modelFactory = new ModelFactory();
        modelFactory.create();

        gameOverlay = new GameOverlay();
        gameOverlay.create();

        Vector3 translation = modelFactory.getInitObject().transform.getTranslation(new Vector3());

        gameCamera = new GameCamera(modelFactory.getSurface());
        gameCamera.position.set(translation.x + 10f, translation.y + 10f, translation.z);
        gameCamera.lookAt(translation);

        gameUnitSpawner = new GameUnitSpawner(this);

        gameSelectItemController = new GameSelectItemController(gameCamera, gamePhysicWorld);

        mainUnitController = new MainUnitController();

        modelFactory.getInitObjects().forEach(gamePhysicWorld::addRigidBody);

        inputMultiplexer.addProcessor(gameSelectItemController);
        inputMultiplexer.addProcessor(gameUnitSpawner);
        inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void render() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

        mainUnitController.update();

        gameCamera.update();

        gameRenderer.render(gameCamera, gamePhysicWorld, gameEnvironment);
        gamePhysicWorld.update(gameCamera, delta);
        gameOverlay.render(gameCamera, gameSelectItemController.getSelectedObject());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        gamePhysicWorld.dispose();
        modelFactory.dispose();
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
        SphereUnit build = new SphereUnit.Builder(modelFactory.getMainModel()).build();

        build.body.setWorldTransform(new Matrix4());
        build.body.translate(modelFactory.getSpawner().transform.getTranslation(new Vector3()));

        gamePhysicWorld.addRigidBody(build);
    }

}
