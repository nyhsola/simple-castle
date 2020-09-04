package com.simple.castle.drawable.scene.event;

import java.util.Map;

public interface SceneEvent {
    void triggerParent(Map<String, Object> map);
    void triggerChild(Map<String, Object> map);
}
