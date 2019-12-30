package com.simple.castle.game.scenes.main.menu.add;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.simple.castle.constants.Scenes;
import com.simple.castle.drawable.scene.Scene;
import com.simple.castle.game.scenes.main.game.add.objects.Camera;
import com.simple.castle.game.scenes.main.menu.MenuScene;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.simple.castle.constants.Constants.DEFAULT_UI_SKIN;

public class MenuSceneBackground extends Scene {

    public static final String CAMERA_POSITION_LABEL = "Camera Position: ";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.00");


    private Stage stage;
    private Skin skin;
    private Label cameraPosition;

    public MenuSceneBackground(Scene parent) {
        super(parent);
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Map<String, Object> map = new HashMap<>();
                map.put(MenuScene.TO_SCENE, Scenes.MENU_SCENE_MENU);
                map.put(MenuScene.TO_BLOCK, Scenes.FULL_GAME_SCENE);
                notifyParent(map);
            }
        });

        cameraPosition = new Label(CAMERA_POSITION_LABEL, skin);

        Table paramsTable = new Table();
        paramsTable.setFillParent(true);
        paramsTable.top().align(Align.topRight).add(cameraPosition);

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.top().align(Align.topLeft).add(menuButton);

        stage = new Stage(new ScreenViewport());
        stage.addActor(buttonTable);
        stage.addActor(paramsTable);
        this.setInputProcessor(stage);
    }

    @Override
    public void onParentEvent(Map<String, Object> map) {
        if (map.containsKey(Camera.CAMERA_POSITION)) {
            Vector3 position = (Vector3) map.get(Camera.CAMERA_POSITION);
            cameraPosition.setText(
                    new StringBuilder(CAMERA_POSITION_LABEL).append(" ")
                            .append(DECIMAL_FORMAT.format(position.x)).append(" ")
                            .append(DECIMAL_FORMAT.format(position.y)).append(" ")
                            .append(DECIMAL_FORMAT.format(position.z)));
        }
    }

    @Override
    public void render() {
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
