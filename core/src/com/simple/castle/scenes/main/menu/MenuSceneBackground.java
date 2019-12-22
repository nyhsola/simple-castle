package com.simple.castle.scenes.main.menu;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.simple.castle.constants.Scenes;
import com.simple.castle.scene.Scene;

public class MenuSceneBackground extends Scene {

    private final FileHandle skinFileHandle;

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton menuButton;

    public MenuSceneBackground(FileHandle skinFileHandle) {
        this.skinFileHandle = skinFileHandle;
    }

    @Override
    public void create() {
        stage = new Stage();
        skin = new Skin(skinFileHandle);
        table = new Table();
        menuButton = new TextButton("Menu", skin);

        table.setFillParent(true);
        table.top().align(Align.topLeft).add(menuButton);

        stage.addActor(table);

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getManagerContext().setCurrentScene(Scenes.MENU_SCENE_MENU);
            }
        });
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

    @Override
    public void render() {
        stage.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

}
