package com.simple.castle.drawable.scene;

import java.util.Map;

public interface SceneEvent {
    void notifyParent(Map<String, Object> map);

    void onParentEvent(Map<String, Object> map);

    void notifyAllChildren(Map<String, Object> map);

    void onChildEvent(Map<String, Object> map);
}
