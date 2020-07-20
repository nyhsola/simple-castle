package com.simple.castle.client.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.client.game.GameScreen;
import com.simple.castle.core.render.BaseRenderer;

public class ClientGame extends Game {
    private BaseRenderer baseRenderer;
    private GameScreen gameScreen;

    @Override
    public void create() {
        baseRenderer = new BaseRenderer(new ModelBatch());

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

}