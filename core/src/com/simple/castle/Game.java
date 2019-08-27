package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Game extends ApplicationAdapter {

    private Stage stage;

    @Override
    public void create() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

		TextButton button = new TextButton("CLICK", skin);
        Slider slider = new Slider(0, 100, 1, false, skin);

        Table table = new Table();
        table.add(button);

        table.row();
        table.add(slider);

        table.setFillParent(true);

        stage = new Stage();
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
