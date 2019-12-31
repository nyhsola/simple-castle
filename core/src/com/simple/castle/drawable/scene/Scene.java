package com.simple.castle.drawable.scene;

public abstract class Scene extends SceneBaseEvent {

    public Scene() {
        super();
    }

    public Scene(Scene parent) {
        super(parent);
    }

}
