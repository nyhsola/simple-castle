package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Game extends ApplicationAdapter {

    private static final String DEFAULT_UI_SKIN = "ui/uiskin.json";
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private Stage stage;

    private Slider slider;
    private Label labelCurrentValue;

    @Override
    public void create() {
        Skin skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        stage = new Stage();

        Table optionsTable = new Table();
        slider = new Slider(0, 555, 1, false, skin);
        Label labelCameraView = new Label("Camera view", skin);
        labelCurrentValue = new Label("", skin);

        optionsTable.top();
        optionsTable.setFillParent(true);

        optionsTable.add(labelCameraView);
        optionsTable.add(slider);
        optionsTable.add(labelCurrentValue);

        Table exitTable = new Table();
        TextButton exitButton = new TextButton("Exit", skin);

        exitTable.bottom();
        exitTable.setFillParent(true);
        exitTable.add(exitButton);
        exitButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return false;
            }
        });

        stage.addActor(optionsTable);
        stage.addActor(exitTable);

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
