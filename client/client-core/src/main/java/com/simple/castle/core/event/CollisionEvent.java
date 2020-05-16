package com.simple.castle.core.event;

import com.simple.castle.core.object.unit.abs.AbstractGameObject;

public interface CollisionEvent {
    void collisionEvent(AbstractGameObject object1, AbstractGameObject object2);
}
