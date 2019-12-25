package com.simple.castle.scenes.main.menu.add;

import java.util.HashMap;
import java.util.Map;

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
import com.simple.castle.scenes.main.menu.MenuScene;

public class MenuSceneBackground extends Scene {

    private final FileHandle skinFileHandle;

    private Stage stage;
    private Skin skin;

    public MenuSceneBackground(Scene parent, FileHandle skinFileHandle) {
        super(parent);
        this.skinFileHandle = skinFileHandle;
    }

    @Override
    public void create() {
        skin = new Skin(skinFileHandle);

        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Map<String, Object> map = new HashMap<>();
                map.put(MenuScene.TO_SCENE, Scenes.MENU_SCENE_MENU);
                map.put(MenuScene.TO_BLOCK, Scenes.FULL_GAME_SCENE);
                toParent(map);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().align(Align.topLeft).add(menuButton);

        stage = new Stage();
        stage.addActor(table);
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
