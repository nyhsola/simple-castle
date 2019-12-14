package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Game extends ApplicationAdapter {

    private static final String DEFAULT_UI_SKIN = "ui/uiskin.json";
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private Skin skin;

    private Stage stage;
    private Table table;

    private Slider slider;
    private Label labelCurrentValue;
    private Label labelCameraView;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        stage = new Stage();
        table = new Table();

        slider = new Slider(0, 555, 1, false, skin);
        labelCameraView = new Label("Camera view", skin);
        labelCurrentValue = new Label("", skin);

        table.top();
        table.setFillParent(true);

        table.add(labelCameraView);
        table.add(slider);
        table.add(labelCurrentValue);

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
