package com.simple.castle.listener;

import com.simple.castle.object.unit.abs.AbstractGameObject;

public interface SceneObjectManager {
    void remove(AbstractGameObject abstractGameObject);

    void add(AbstractGameObject abstractGameObject);

    AbstractGameObject getByName(String name);

    AbstractGameObject getByUserData(String userData);

    boolean contains(AbstractGameObject abstractGameObject);

    boolean contains(String userData);
}
