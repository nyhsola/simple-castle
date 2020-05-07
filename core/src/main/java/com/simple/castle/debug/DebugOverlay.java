package com.simple.castle.debug;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.simple.castle.event.DoEvery;
import com.simple.castle.scene.game.controller.PlayerController;
import com.simple.castle.utils.AssetLoader;

import java.util.Locale;

public class DebugOverlay {

    public final DebugInformation debugInformation = new DebugInformation();

    private final Stage stage;
    private final Skin skin;
    private final TextButton timeButton;
    private final TextButton fpsButton;
    private final TextButton totalUnitsButton;
    private final com.simple.castle.event.DoEvery doEvery = new DoEvery(500);

    public DebugOverlay() {
        skin = AssetLoader.loadSkin();

        Label timeLabel = new Label("Spawn in: ", skin);
        Label fpsLabel = new Label("FPS: ", skin);
        Label totalUnitsLabel = new Label("Total units: ", skin);

        timeButton = new TextButton(Long.toString(PlayerController.spawnEvery), skin);
        fpsButton = new TextButton("", skin);
        totalUnitsButton = new TextButton("", skin);

        Table table = new Table();
        table.align(Align.topLeft);
        table.add(timeLabel, timeButton).row();
        table.add(fpsLabel, fpsButton).row();
        table.add(totalUnitsLabel, totalUnitsButton).row();

        table.setFillParent(true);

        stage = new Stage(new ScreenViewport());
        stage.addActor(table);

        doEvery.start();
    }

    public void render() {
        doEvery.update(() -> {
            timeButton.setText(String.format(Locale.getDefault(), "%s", debugInformation.getTimeLeft()));
            fpsButton.setText(String.format(Locale.getDefault(), "%.2f", debugInformation.getFps()));
            totalUnitsButton.setText(String.format(Locale.getDefault(), "%s", debugInformation.getTotalUnits()));
        });
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
