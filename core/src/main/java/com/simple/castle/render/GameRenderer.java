package com.simple.castle.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.object.constructors.SceneObjectsHandler;
import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.List;
import java.util.stream.Collectors;

public class GameRenderer {

    private final ModelBatch modelBatch;

    public GameRenderer() {
        modelBatch = new ModelBatch();
    }

    public void render(GameCamera gameCamera, SceneObjectsHandler sceneObjectsHandler, GameEnvironment gameEnvironment) {
        modelBatch.begin(gameCamera);
        modelBatch.render(sceneObjectsHandler.getDrawObjects(), gameEnvironment.getEnvironment());
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}
