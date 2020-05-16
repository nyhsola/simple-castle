package com.simple.castle.core.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.simple.castle.core.object.constructors.SceneObjectsHandler;

public class BaseRenderer {
    private final ModelBatch modelBatch;

    public BaseRenderer() {
        modelBatch = new ModelBatch();
    }

    public void render(BaseCamera baseCamera, SceneObjectsHandler sceneObjectsHandler, BaseEnvironment baseEnvironment) {
        modelBatch.begin(baseCamera);
        modelBatch.render(sceneObjectsHandler.getDrawObjects(), baseEnvironment.getEnvironment());
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }
}
