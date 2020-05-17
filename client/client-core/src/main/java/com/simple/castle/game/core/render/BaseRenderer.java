package com.simple.castle.game.core.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.server.game.core.object.constructors.SceneObjectsHandler;

public class BaseRenderer extends AbstractRenderer {
    private final ModelBatch modelBatch;

    public BaseRenderer() {
        modelBatch = new ModelBatch();
    }

    @Override
    public void render(BaseCamera baseCamera, SceneObjectsHandler sceneObjectsHandler, BaseEnvironment baseEnvironment) {
        modelBatch.begin(baseCamera);
        modelBatch.render(sceneObjectsHandler.getDrawObjects(), baseEnvironment.getEnvironment());
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
