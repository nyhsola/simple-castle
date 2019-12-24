package com.simple.castle.scenes.main.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;

public class MenuSceneMain extends Scene {

    private final FileHandle skinFileHandle;

    private Stage stage;
    private Skin skin;
    private Table settingsTable;
    private Slider slider;
    private Label labelCameraView;
    private Label labelCurrentValue;

    private Table backButtonTable;
    private TextButton backButton;

    public MenuSceneMain(Scene parent, FileHandle skinFileHandle) {
        super(parent);
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
                Map<String, Object> map = new HashMap<>();
                map.put(MenuScene.TO_SCENE, Scenes.MENU_SCENE_BACKGROUND);
                map.put(MenuScene.TO_UNBLOCK, Scenes.GAME_SCENE);
                triggerParent(map);
            }
        });

        triggerParent(Collections.singletonMap(MenuScene.CAMERA_FIELD_OF_VIEW, slider.getValue()));
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                triggerParent(Collections.singletonMap(MenuScene.CAMERA_FIELD_OF_VIEW, slider.getValue()));
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
