package com.simple.castle.server.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.core.ServerState;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.server.screen.ServerScreen;

import static org.mockito.Mockito.mock;

public final class ServerGame extends Game {

    private final boolean isGUI;

    private BaseRenderer baseRenderer;
    private ServerScreen serverScreen;

    public ServerGame(boolean isGUI) {
        this.isGUI = isGUI;
    }

    @Override
    public void create() {
        Bullet.init();
        baseRenderer = new BaseRenderer(isGUI ? new ModelBatch() : mock(ModelBatch.class));
        serverScreen = new ServerScreen(baseRenderer);

        this.setScreen(serverScreen);

        Gdx.input.setInputProcessor(serverScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        serverScreen.dispose();
        baseRenderer.dispose();
    }

    public ServerState getState() {
        return new ServerState(serverScreen.getState());
    }
}