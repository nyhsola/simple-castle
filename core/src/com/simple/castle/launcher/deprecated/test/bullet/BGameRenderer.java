package com.simple.castle.launcher.deprecated.test.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class BGameRenderer extends ApplicationAdapter {

    private ModelBatch modelBatch;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
    }

    public void render(BGameCamera bGameCamera, BPhysicWorld bPhysicWorld, BEnvironment bEnvironment) {
        modelBatch.begin(bGameCamera.getCam());
        modelBatch.render(bPhysicWorld.getInstances(), bEnvironment.getEnvironment());
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
