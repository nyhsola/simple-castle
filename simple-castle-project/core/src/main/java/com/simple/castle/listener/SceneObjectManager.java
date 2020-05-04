package com.simple.castle.listener;

import com.simple.castle.object.unit.absunit.AbstractGameObject;

public interface SceneObjectManager {
    void remove(AbstractGameObject abstractGameObject);

    void add(AbstractGameObject abstractGameObject);
}
