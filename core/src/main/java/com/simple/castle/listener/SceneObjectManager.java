package com.simple.castle.listener;

import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.List;

public interface SceneObjectManager {
    void remove(AbstractGameObject abstractGameObject);

    void add(AbstractGameObject abstractGameObject);

    void addAll(List<? extends AbstractGameObject> abstractGameObjects);

    AbstractGameObject getByModelName(String name);

    boolean contains(AbstractGameObject abstractGameObject);
}
