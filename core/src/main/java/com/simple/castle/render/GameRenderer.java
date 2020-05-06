package com.simple.castle.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.Collection;
import java.util.stream.Collectors;

public class GameRenderer {

    private final ModelBatch modelBatch;

    public GameRenderer() {
        modelBatch = new ModelBatch();
    }

    public void render(GameCamera gameCamera, Collection<AbstractGameObject> gameObjects, GameEnvironment gameEnvironment) {
        modelBatch.begin(gameCamera);
        modelBatch.render(
                gameObjects.stream()
                        .filter(abstractGameObject -> !abstractGameObject.hide)
                        .collect(Collectors.toList()),
                gameEnvironment.getEnvironment());
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}