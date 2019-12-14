package com.simple.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuScene extends ApplicationAdapter {

    private static final String DEFAULT_UI_SKIN = "ui/uiskin.json";

    private Stage stage;
    private Skin skin;
    private Table optionsTable;
    private Slider slider;
    private Label labelCameraView;
    private Label labelCurrentValue;

    @Override
    public void create() {
        stage = new Stage();

        skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        optionsTable = new Table();

        slider = new Slider(0, 555, 1, false, skin);
        labelCameraView = new Label("Camera view", skin);
        labelCurrentValue = new Label("", skin);

        optionsTable.top();
        optionsTable.setFillParent(true);
        optionsTable.add(labelCameraView);
        optionsTable.add(slider);
        optionsTable.add(labelCurrentValue);

        stage.addActor(optionsTable);
    }

    @Override
    public void render() {
        labelCurrentValue.setText(Float.toString(slider.getValue()));
        stage.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

}
