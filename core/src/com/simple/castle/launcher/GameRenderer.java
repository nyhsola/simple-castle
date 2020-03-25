package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.List;

public class GameRenderer extends ApplicationAdapter {
    private static final Color CLEAR_COLOR = new Color(0.376f, 0.4f, 0.4f, 1);

    private final GameCamera gameCamera;
    private final WorldSettings worldSettings;
    private List<ModelInstance> modelInstanceList;
    private ModelBatch modelBatch;

    public GameRenderer(GameCamera gameCamera, WorldSettings worldSettings, List<ModelInstance> modelInstanceList) {
        this.gameCamera = gameCamera;
        this.worldSettings = worldSettings;
        this.modelInstanceList = modelInstanceList;
        modelBatch = new ModelBatch();
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(gameCamera.getCamera());
        modelBatch.render(modelInstanceList, worldSettings.getEnvironment());
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
