package com.simple.castle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.MainScene;

public class Game extends Scene {
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        manager
                .addScene(Scenes.MAIN_SCENE, new MainScene(this))
                .setCurrentScene(Scenes.MAIN_SCENE);
        super.create();
    }

    @Override
    public void render() {
        super.update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        super.render();
    }
}
