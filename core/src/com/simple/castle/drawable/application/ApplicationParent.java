package com.simple.castle.drawable.application;

import com.simple.castle.drawable.scene.Scene;

public abstract class ApplicationParent extends ApplicationDrawable {
    protected final Scene parent;

    public ApplicationParent() {
        this.parent = null;
    }

    public ApplicationParent(Scene parent) {
        this.parent = parent;
    }
}
