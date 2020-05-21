package com.simple.castle.server.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.server.composition.BaseObject;

import java.util.List;

import static org.mockito.Mockito.mock;

public class BaseRenderer {
    private final ModelBatch modelBatch;

    public BaseRenderer(boolean isGUI) {
        modelBatch = isGUI ? new ModelBatch() : mock(ModelBatch.class);
    }

    public void render(Camera camera, List<BaseObject> baseObjectList, BaseEnvironment baseEnvironment) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        for (BaseObject baseObject : baseObjectList) {
            modelBatch.render(baseObject.getModelInstance(), baseEnvironment.getEnvironment());

        }
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}
