package com.simple.castle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.simple.castle.ChangeScene;
import com.simple.castle.Manager;

import static com.simple.castle.Constants.DEFAULT_UI_SKIN;

public class MenuScene extends Scene {

    private Stage stage;
    private Skin skin;
    private Table optionsTable;
    private Slider slider;
    private Label labelCameraView;
    private Label labelCurrentValue;
    private TextButton backButton;
    private ChangeScene changeScene;

    public MenuScene(ChangeScene changeScene) {
        this.changeScene = changeScene;
    }

    @Override
    public void create() {
        stage = new Stage();

        skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));

        optionsTable = new Table();

        slider = new Slider(0, 555, 1, false, skin);
        labelCameraView = new Label("Camera view", skin);
        labelCurrentValue = new Label("", skin);
        backButton = new TextButton("Back", skin);

        optionsTable.setFillParent(true);

        optionsTable.align(Align.topLeft).add(backButton);
        optionsTable.row().top();
        optionsTable.add();
        optionsTable.add(labelCameraView);
        optionsTable.add(slider);
        optionsTable.add(labelCurrentValue);

        stage.addActor(optionsTable);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeScene.setScene(Manager.MAIN_SCENE);
            }
        });
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
    public boolean keyDown(int keycode) {
        return stage.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return stage.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return stage.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return stage.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return stage.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return stage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return stage.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return stage.scrolled(amount);
    }

}
