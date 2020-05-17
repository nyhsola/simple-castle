package com.simple.castle.server.game.core.settings.dto;

import com.simple.castle.server.game.core.settings.dto.base.ObjectConstructorJson;

import java.util.List;

public class ObjectConstructorsJson {
    private List<ObjectConstructorJson> objectConstructors;

    public List<ObjectConstructorJson> getObjectConstructors() {
        return objectConstructors;
    }

    public void setObjectConstructors(List<ObjectConstructorJson> objectConstructors) {
        this.objectConstructors = objectConstructors;
    }
}
