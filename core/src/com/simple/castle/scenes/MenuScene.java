package com.simple.castle.scenes;

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

        slider = new Slider(0, 555, 1, false, skin);
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
                managerController.getChangeScene().changeScene(MainScene.MENU_BACKGROUND_SCENE);
                managerController.getBlockScene().deleteBlockScene(MainScene.GAME_SCENE);
            }
        });

        this.setInputProcessor(stage);
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
