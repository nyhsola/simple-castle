package com.simple.castle.game.core.manager;

import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.server.game.core.debug.DebugInformation;
import com.simple.castle.server.game.core.event.CollisionEvent;

public interface Controller extends CollisionEvent, Disposable {
    void update();

    DebugInformation getDebugInfo();
}
