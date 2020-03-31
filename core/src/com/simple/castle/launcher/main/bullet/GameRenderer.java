package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class GameRenderer extends ApplicationAdapter {

    private ModelBatch modelBatch;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
    }

    public void render(GameCamera gameCamera, PhysicWorld physicWorld, GameEnvironment gameEnvironment) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(gameCamera.getPerspectiveCamera());
        modelBatch.render(physicWorld.getInstances(), gameEnvironment.getEnvironment());
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
