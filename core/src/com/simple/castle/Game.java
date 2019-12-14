package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter {

    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private Menu menu;

    @Override
    public void create() {
        menu = new Menu();
        Gdx.input.setInputProcessor(menu);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menu.draw();
    }

    @Override
    public void dispose() {
        menu.dispose();
    }
}
