package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.simple.castle.game.Game;

public class GameLauncher implements ApplicationListener {

    private final Game game = new Game();

    @Override
    public void create() {
        game.create();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    @Override
    public void render() {
        game.update(Gdx.graphics.getDeltaTime());
        game.render();
    }

    @Override
    public void pause() {
        game.pause();
    }

    @Override
    public void resume() {
        game.resume();
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
