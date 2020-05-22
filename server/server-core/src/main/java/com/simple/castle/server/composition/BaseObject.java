package com.simple.castle.server.composition;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.server.physic.unit.PhysicObject;

public class BaseObject implements Disposable {
    private final String id;
    private final PhysicObject physicObject;
    private final ModelInstance modelInstance;
    private Boolean hide;

    public BaseObject(Constructor constructor) {
        this.id = constructor.getId();

        this.physicObject = constructor.buildPhysic();
        this.modelInstance = constructor.buildModel();

        this.physicObject.getMotionState().setWorldTransform(modelInstance.transform);

        this.hide = constructor.getHide();
    }

    public String getId() {
        return id;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public PhysicObject getPhysicObject() {
        return physicObject;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    @Override
    public void dispose() {
        physicObject.dispose();
    }
}
