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

public class MenuBackgroundScene extends Scene {

    private final FileHandle skinFileHandle;

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton menuButton;

    public MenuBackgroundScene(FileHandle skinFileHandle) {
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
                getManagerContext().setCurrentScene(Scenes.MENU_SCENE);
                getManagerContext().getBlockInput().add(Scenes.GAME_SCENE);
            }
        });
        this.setInputProcessor(stage);
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
