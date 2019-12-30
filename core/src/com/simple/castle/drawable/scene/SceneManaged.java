package com.simple.castle.drawable.scene;

import com.simple.castle.drawable.ApplicationDrawable;
import com.simple.castle.drawable.manager.Manager;
import com.simple.castle.drawable.scene.event.SceneEvent;

import java.util.Map;

public abstract class SceneManaged extends ApplicationDrawable implements SceneEvent {

    protected final Manager manager = new Manager();
    protected final Scene parent;

    public SceneManaged() {
        this.parent = null;
    }

    public SceneManaged(Scene parent) {
        this.parent = parent;
    }

    @Override
    public void notifyAllChildren(Map<String, Object> map) {
        manager.notifyAllChildren(map);
    }

    @Override
    public void notifyParent(Map<String, Object> map) {
        if (parent != null) {
            parent.onChildEvent(map);
        }
    }

    @Override
    public void onParentEvent(Map<String, Object> map) {
        this.notifyAllChildren(map);
    }

    @Override
    public void onChildEvent(Map<String, Object> map) {
        this.notifyParent(map);
    }
}
