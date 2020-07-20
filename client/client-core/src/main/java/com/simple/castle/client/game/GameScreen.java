package com.simple.castle.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.SocketHints;
import com.simple.castle.core.ServerState;
import com.simple.castle.core.api.ServerApi;
import com.simple.castle.core.asset.AssetLoader;
import com.simple.castle.core.render.BaseCamera;
import com.simple.castle.core.render.BaseEnvironment;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.core.screen.BaseScreen;

public class GameScreen extends BaseScreen {
    private final BaseRenderer baseRenderer;
    private final BaseCamera baseCamera;
    private final BaseEnvironment baseEnvironment;

    private final ClientSceneManager clientSceneManager;

    private final ServerApi serverApi;
    private final Model model;

    public GameScreen(BaseRenderer baseRenderer) {
        this.model = new AssetLoader().loadModel();

        this.baseRenderer = baseRenderer;
        this.baseCamera = new BaseCamera(new Vector3(10, 10, 10), null);
        this.baseEnvironment = new BaseEnvironment();

        this.clientSceneManager = new ClientSceneManager(model);

        this.serverApi = new ServerApi(Gdx.net.newClientSocket(Net.Protocol.TCP, "127.0.0.1", 9090, new SocketHints()));

        this.inputMultiplexer.addProcessor(baseCamera);
    }

    @Override
    public void render(float delta) {
        baseCamera.update(delta);

        ServerState tick = serverApi.getPositions();

        Gdx.app.log("GameScreen", tick.toString());

        baseRenderer.render(baseCamera, clientSceneManager.updateAndGet(tick), baseEnvironment);
    }

    @Override
    public void resize(int width, int height) {
        baseCamera.resize(width, height);
        baseCamera.update();
    }

    @Override
    public void dispose() {
        serverApi.dispose();
        model.dispose();
        baseRenderer.dispose();
    }

}
