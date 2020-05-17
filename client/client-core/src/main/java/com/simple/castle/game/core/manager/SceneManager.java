package com.simple.castle.game.core.manager;


import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;

import java.util.List;

public interface SceneManager {
    void remove(AbstractGameObject abstractGameObject);

    void add(AbstractGameObject abstractGameObject);

    void addAll(List<? extends AbstractGameObject> abstractGameObjects);

    AbstractGameObject getByModelName(String name);

    boolean contains(AbstractGameObject abstractGameObject);
}
