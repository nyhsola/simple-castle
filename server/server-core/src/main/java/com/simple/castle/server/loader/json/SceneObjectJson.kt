package com.simple.castle.server.loader.json;

import com.simple.castle.server.kt.composition.InteractType;
import com.simple.castle.server.kt.composition.PhysicShape;

public class SceneObjectJson {
    private String nodePattern;
    private PhysicShape shape;
    private Float mass;
    private InteractType interact;
    private Boolean instantiate;
    private Boolean hide;

    public SceneObjectJson() {
    }

    public SceneObjectJson(SceneObjectJson sceneObjectJson) {
        this.nodePattern = sceneObjectJson.nodePattern;
        this.shape = sceneObjectJson.shape;
        this.mass = sceneObjectJson.mass;
        this.interact = sceneObjectJson.interact;
        this.instantiate = sceneObjectJson.instantiate;
        this.hide = sceneObjectJson.hide;
    }

    public String getNodePattern() {
        return nodePattern;
    }

    public void setNodePattern(String nodePattern) {
        this.nodePattern = nodePattern;
    }

    public PhysicShape getShape() {
        return shape;
    }

    public void setShape(PhysicShape shape) {
        this.shape = shape;
    }

    public Float getMass() {
        return mass;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public InteractType getInteract() {
        return interact;
    }

    public void setInteract(InteractType interact) {
        this.interact = interact;
    }

    public Boolean getInstantiate() {
        return instantiate;
    }

    public void setInstantiate(Boolean instantiate) {
        this.instantiate = instantiate;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }
}
