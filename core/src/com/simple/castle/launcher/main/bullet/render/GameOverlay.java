package com.simple.castle.launcher.main.bullet.render;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverlay extends ApplicationAdapter {

    private BitmapFont bitmapFont;
    private SpriteBatch batch;
    private CharSequence str;

    @Override
    public void create() {
        bitmapFont = new BitmapFont();
        batch = new SpriteBatch();
        str = "Hello World!";
    }

    @Override
    public void render() {
        batch.begin();
        bitmapFont.draw(batch, str, 0, 10);
        bitmapFont.draw(batch, str, 0, 20);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }
}
