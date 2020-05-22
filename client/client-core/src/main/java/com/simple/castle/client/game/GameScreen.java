package com.simple.castle.client.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.base.ModelSend;
import com.simple.castle.base.asset.AssetLoader;
import com.simple.castle.base.render.BaseCamera;
import com.simple.castle.base.render.BaseEnvironment;
import com.simple.castle.base.render.BaseRenderer;
import com.simple.castle.client.server.ServerConnector;

import java.util.List;

public class GameScreen extends ScreenAdapter implements InputProcessor {
    private final BaseRenderer baseRenderer;
    private final BaseCamera baseCamera;
    private final BaseEnvironment baseEnvironment;

    private final ClientSceneManager clientSceneManager;

    private final ServerConnector serverConnector;
    private final Model model;

    private final InputMultiplexer inputMultiplexer;

    public GameScreen(BaseRenderer baseRenderer) {
        this.model = new AssetLoader().loadModel();

        this.baseRenderer = baseRenderer;
        this.baseCamera = new BaseCamera(new Vector3(10, 10, 10));
        this.baseEnvironment = new BaseEnvironment();

        this.clientSceneManager = new ClientSceneManager(model);

        this.serverConnector = new ServerConnector("127.0.0.1", 9090);

        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(baseCamera);
    }

    @Override
    public void render(float delta) {
        baseCamera.update(delta);

        List<ModelSend> tick = serverConnector.getNextWorldTick(1000 / 60);

        baseRenderer.render(
                baseCamera,
                clientSceneManager.updateAndGet(tick),
                baseEnvironment);

//        Gdx.app.log("GameScreen", String.valueOf(tick));
    }

    @Override
    public void resize(int width, int height) {
        baseCamera.resize(width, height);
        baseCamera.update();
    }

    @Override
    public void dispose() {
        serverConnector.dispose();
        model.dispose();
        baseRenderer.dispose();
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
