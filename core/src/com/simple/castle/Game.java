package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Game extends ApplicationAdapter {

    private static final String DEFAULT_UI_SKIN = "ui/uiskin.json";
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private Stage stage;
    private Label labelCurrentValue;
    private Slider slider;

    @Override
    public void create() {
        Skin skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        Label labelCameraView = new Label("Camera view", skin);
        slider = new Slider(0, 555, 1, false, skin);

        Table table = new Table();
        table.top();

        table.add(labelCameraView);
        table.add(slider).expandX().fillX();

        labelCurrentValue = new Label(Float.toString(slider.getValue()), skin);

        table.row();
        table.add(labelCurrentValue).fillX();

        table.setFillParent(true);

        stage = new Stage();
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        labelCurrentValue.setText(Float.toString(slider.getValue()));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
