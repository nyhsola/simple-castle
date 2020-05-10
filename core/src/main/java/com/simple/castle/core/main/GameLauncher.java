package com.simple.castle.core.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.core.render.BaseRenderer;
import com.simple.castle.scene.game.GameScene;

public class GameLauncher extends Game {

    private com.simple.castle.core.render.BaseRenderer baseRenderer;

    @Override
    public void create() {
        Bullet.init();

        baseRenderer = new BaseRenderer();
        GameScene gameScene = new GameScene(baseRenderer);

        setScreen(gameScene);
        Gdx.input.setInputProcessor(gameScene);
    }

    @Override
    public void dispose() {
        baseRenderer.dispose();
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