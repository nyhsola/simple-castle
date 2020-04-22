package com.simple.castle.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
}