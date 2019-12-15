package com.simple.castle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.simple.castle.ChangeScene;
import com.simple.castle.Manager;

import static com.simple.castle.Constants.DEFAULT_UI_SKIN;

public class MainScene extends Scene {

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton textButton;

    private ChangeScene changeScene;

    public MainScene(ChangeScene changeScene) {
        this.changeScene = changeScene;
    }

    @Override
    public void create() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal(DEFAULT_UI_SKIN));
        table = new Table();
        textButton = new TextButton("Menu", skin);

        table.setFillParent(true);
        table.top().align(Align.topLeft).add(textButton);

        stage.addActor(table);

        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeScene.setScene(Manager.MENU_SCENE);
            }
        });
    }

    @Override
    public void render() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
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
