package com.simple.castle.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.object.unit.absunit.AbstractGameObject;

import java.util.Collection;

public class GameRenderer {

    private final ModelBatch modelBatch;

    public GameRenderer() {
        modelBatch = new ModelBatch();
    }

    public void render(GameCamera gameCamera, Collection<AbstractGameObject> gameObjects, GameEnvironment gameEnvironment) {
        modelBatch.begin(gameCamera);
        modelBatch.render(gameObjects, gameEnvironment.getEnvironment());
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}
