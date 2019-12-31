package com.simple.castle.drawable.scene;

import com.simple.castle.drawable.scene.event.SceneEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Scene extends SceneManaged implements SceneEvent {

    public static final String CHANGE_SCENE = "CHANGE_SCENE";
    public static final String BLOCK_SCENE = "BLOCK_SCENE";

    private final List<SceneEvent> listeners = new ArrayList<>();

    public Scene() {
        super();
    }

    public final Scene addListener(SceneEvent sceneEvent) {
        listeners.add(sceneEvent);
        return this;
    }

    public final Scene removeListener(SceneEvent sceneEvent) {
        listeners.remove(sceneEvent);
        return this;
    }

    public final void notifyListeners(Map<String, Object> eventData) {
        for (SceneEvent listener : listeners) {
            listener.onEvent(eventData);
        }
    }

    @Override
    public void onEvent(Map<String, Object> eventData) {

    }
}
