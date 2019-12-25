package com.simple.castle.scenes.main.menu.add;

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
import com.simple.castle.scenes.main.menu.MenuScene;

public class MenuSceneMain extends Scene {

    private final FileHandle skinFileHandle;

    private Stage stage;
    private Skin skin;
    private Slider slider;
    private Label labelCurrentValue;

    public MenuSceneMain(Scene parent, FileHandle skinFileHandle) {
        super(parent);
        this.skinFileHandle = skinFileHandle;
    }

    @Override
    public void create() {
        skin = new Skin(skinFileHandle);

        slider = new Slider(0, 180, 1, false, skin);
        slider.setValue(67);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toParent(Collections.singletonMap(MenuScene.CAMERA_FIELD_OF_VIEW, slider.getValue()));
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Map<String, Object> map = new HashMap<>();
                map.put(MenuScene.TO_SCENE, Scenes.MENU_SCENE_BACKGROUND);
                map.put(MenuScene.TO_UNBLOCK, Scenes.FULL_GAME_SCENE);
                toParent(map);
            }
        });

        Label labelCameraView = new Label("Camera view", skin);
        labelCurrentValue = new Label("", skin);

        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.top();
        settingsTable.add();
        settingsTable.add(labelCameraView);
        settingsTable.add(slider);
        settingsTable.add(labelCurrentValue);

        Table backButtonTable = new Table();
        backButtonTable.setFillParent(true);
        backButtonTable.top().align(Align.topLeft).add(backButton);

        toParent(Collections.singletonMap(MenuScene.CAMERA_FIELD_OF_VIEW, slider.getValue()));

        stage = new Stage();
        stage.addActor(settingsTable);
        stage.addActor(backButtonTable);
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
