package com.simple.castle.drawable.scene;

import java.util.Map;

public interface SceneEvent {
    void toParent(Map<String, Object> map);
    void fromParent(Map<String, Object> map);
    void toChild(Map<String, Object> map);
    void fromChild(Map<String, Object> map);
}
