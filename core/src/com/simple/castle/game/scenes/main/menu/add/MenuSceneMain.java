package com.simple.castle.game.scenes.main.menu.add;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.drawable.scene.SceneBaseEvent;
import com.simple.castle.game.scenes.main.menu.MenuScene;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

public class MenuSceneMain extends Scene {

    private Stage stage;
    private Skin skin;
    private Slider slider;
    private Label labelCurrentValue;

    public MenuSceneMain(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        slider = new Slider(0, 180, 1, false, skin);
        slider.setValue(67);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                notifyParent(Collections.singletonMap(MenuScene.CAMERA_FIELD_OF_VIEW, slider.getValue()));
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Map<String, Object> map = new HashMap<>();
                map.put(SceneBaseEvent.TO_SCENE, Scenes.MENU_SCENE_BACKGROUND);
                map.put(SceneBaseEvent.TO_UNBLOCK, Scenes.FULL_GAME_SCENE);
                notifyParent(map);
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

        notifyParent(Collections.singletonMap(MenuScene.CAMERA_FIELD_OF_VIEW, slider.getValue()));

        stage = new Stage(new ScreenViewport());
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

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
