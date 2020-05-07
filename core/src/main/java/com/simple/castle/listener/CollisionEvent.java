package com.simple.castle.listener;

import com.simple.castle.object.unit.abs.AbstractGameObject;

public interface CollisionEvent {
    void collisionEvent(AbstractGameObject object1, AbstractGameObject object2);
}
