package com.simple.castle.drawable.scene.event;

import java.util.Map;

public interface SceneEvent {
    void onEvent(Map<String, Object> eventData);
}
