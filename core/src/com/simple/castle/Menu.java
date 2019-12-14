package com.simple.castle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Menu extends Stage {

    private static final String DEFAULT_UI_SKIN = "ui/uiskin.json";

    private Skin skin;
    private Table optionsTable;
    private Slider slider;
    private Label labelCameraView;
    private Label labelCurrentValue;

    public Menu() {
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

        this.addActor(optionsTable);
    }

    @Override
    public void draw() {
        labelCurrentValue.setText(Float.toString(slider.getValue()));
        super.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
        super.dispose();
    }
}
