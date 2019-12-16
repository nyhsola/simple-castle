package com.simple.castle.scenes.main.menu;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.simple.castle.manager.impl.Scene;
import com.simple.castle.scenes.main.MainScene;

public class MenuScene extends Scene {

    private final FileHandle skinFileHandle;

    private Stage stage;
    private Skin skin;
    private Table settingsTable;
    private Slider slider;
    private Label labelCameraView;
    private Label labelCurrentValue;

    private Table backButtonTable;
    private TextButton backButton;

    public MenuScene(FileHandle skinFileHandle) {
        this.skinFileHandle = skinFileHandle;
    }

    @Override
    public void create() {
        stage = new Stage();

        skin = new Skin(skinFileHandle);

        settingsTable = new Table();

        slider = new Slider(0, 180, 1, false, skin);
        slider.setValue(67);
        labelCameraView = new Label("Camera view", skin);
        labelCurrentValue = new Label("", skin);

        settingsTable.setFillParent(true);

        settingsTable.top();
        settingsTable.add();
        settingsTable.add(labelCameraView);
        settingsTable.add(slider);
        settingsTable.add(labelCurrentValue);

        backButtonTable = new Table();
        backButton = new TextButton("Back", skin);

        backButtonTable.setFillParent(true);
        backButtonTable.top().align(Align.topLeft).add(backButton);

        stage.addActor(settingsTable);
        stage.addActor(backButtonTable);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getManagerContext().setCurrentScene(MainScene.MENU_BACKGROUND_SCENE);
                getManagerContext().getBlockInput().remove(MainScene.GAME_SCENE);
            }
        });

        this.setInputProcessor(stage);
    }

    @Override
    public void update() {
        getManagerContext().getCameraSettings().setFieldOfView(slider.getValue());
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
