package com.simple.castle.game.core.render;

import com.simple.castle.server.game.core.object.constructors.SceneObjectsHandler;

public abstract class AbstractRenderer {
    public abstract void render(BaseCamera baseCamera, SceneObjectsHandler sceneObjectsHandler, BaseEnvironment baseEnvironment);

    public abstract void dispose();
}
