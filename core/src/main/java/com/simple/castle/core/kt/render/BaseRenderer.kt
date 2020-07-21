package com.simple.castle.core.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.Collection;

public class BaseRenderer {
    private final ModelBatch modelBatch;

    public BaseRenderer(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }

    public void render(Camera camera, Collection<ModelInstance> baseObjectList, BaseEnvironment baseEnvironment) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(baseObjectList, baseEnvironment.getEnvironment());
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}
