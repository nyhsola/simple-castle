package com.simple.castle.core.settings.dto;

import com.simple.castle.core.settings.dto.base.SceneObjectJson;

import java.util.List;

public class SceneObjectsJson {
    private List<SceneObjectJson> sceneObjects;

    public List<SceneObjectJson> getSceneObjects() {
        return sceneObjects;
    }

    public void setSceneObjects(List<SceneObjectJson> sceneObjects) {
        this.sceneObjects = sceneObjects;
    }
}
