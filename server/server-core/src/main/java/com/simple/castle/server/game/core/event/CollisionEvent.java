package com.simple.castle.server.game.core.event;

import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;

public interface CollisionEvent {
    void collisionEvent(AbstractGameObject object1, AbstractGameObject object2);
}
