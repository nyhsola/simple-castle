package com.simple.castle.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.render.GameRenderer;
import com.simple.castle.scene.game.GameScene;

public class GameLauncher extends Game {

    private GameRenderer gameRenderer;

    @Override
    public void create() {
        Bullet.init();

        gameRenderer = new GameRenderer();
        GameScene gameScene = new GameScene(gameRenderer);

        setScreen(gameScene);
        Gdx.input.setInputProcessor(gameScene);
    }

    @Override
    public void dispose() {
        gameRenderer.dispose();
        super.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        super.render();
    }
}