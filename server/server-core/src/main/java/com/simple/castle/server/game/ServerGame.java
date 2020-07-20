package com.simple.castle.server.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.core.ServerState;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.server.screen.GameScreen;

import static org.mockito.Mockito.mock;

public final class ServerGame extends Game {

    private final boolean isGUI;

    private BaseRenderer baseRenderer;
    private GameScreen gameScreen;

    public ServerGame(boolean isGUI) {
        this.isGUI = isGUI;
    }

    @Override
    public void create() {
        Bullet.init();
        baseRenderer = new BaseRenderer(isGUI ? new ModelBatch() : mock(ModelBatch.class));
        gameScreen = new GameScreen(baseRenderer);

        this.setScreen(gameScreen);

        Gdx.input.setInputProcessor(gameScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        gameScreen.dispose();
        baseRenderer.dispose();
    }

    public ServerState getState() {
        return new ServerState(gameScreen.getState());
    }
}