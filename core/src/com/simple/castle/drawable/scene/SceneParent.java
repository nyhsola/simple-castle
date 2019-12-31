package com.simple.castle.drawable.scene;

import com.simple.castle.drawable.ApplicationDrawable;

public abstract class SceneParent extends ApplicationDrawable {
    protected final Scene parent;

    public SceneParent() {
        this.parent = null;
    }

    public SceneParent(Scene parent) {
        this.parent = parent;
    }
}
