package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;
import com.simple.castle.launcher.main.bullet.scene.game.GameScene;
import com.simple.castle.launcher.main.utils.ModelLoader;

public class GameLauncher extends Game {

    private GameRenderer gameRenderer;
    private GameModels gameModels;
    private Model model;

    @Override
    public void create() {
        Bullet.init();

        model = ModelLoader.loadModel();

        gameRenderer = new GameRenderer();
        gameModels = new GameModels(model);

        GameScene gameScene = new GameScene(gameRenderer, gameModels);

        setScreen(gameScene);
        Gdx.input.setInputProcessor(gameScene);
    }

    @Override
    public void dispose() {
        gameRenderer.dispose();
        gameModels.dispose();
        model.dispose();

        super.dispose();
    }
}