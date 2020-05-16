package com.simple.castle.core.manager;

import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.core.debug.DebugInformation;
import com.simple.castle.core.event.CollisionEvent;

public interface Controller extends CollisionEvent, Disposable {
    void update();

    DebugInformation getDebugInfo();
}
