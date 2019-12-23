package com.simple.castle.scene;

import java.util.Map;

public interface SceneEvent {
    void childSceneEvent(Map<String, Object> map);
}
