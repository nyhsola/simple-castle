package com.simple.castle.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.object.unit.absunit.AbstractGameObject;

import java.util.Collection;

public class GameRenderer {

    private final ModelBatch modelBatch;

    public GameRenderer() {
        modelBatch = new ModelBatch();
    }

    public void render(GameCamera gameCamera, Collection<AbstractGameObject> gameObjects, GameEnvironment gameEnvironment) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(gameCamera);
        modelBatch.render(gameObjects, gameEnvironment.getEnvironment());
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}
