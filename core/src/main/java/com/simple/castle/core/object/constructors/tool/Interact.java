package com.simple.castle.core.object.constructors.tool;

import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.object.unit.add.ObjectConstructor;
import com.simple.castle.core.object.unit.basic.ActiveGameObject;
import com.simple.castle.core.object.unit.basic.GhostGameObject;
import com.simple.castle.core.object.unit.basic.KinematicGameObject;

public enum Interact {
    ACTIVE,
    KINEMATIC,
    GHOST;

    public AbstractGameObject build(ObjectConstructor objectConstructor) {
        switch (this) {
            case ACTIVE:
                return new ActiveGameObject(objectConstructor);
            case KINEMATIC:
                return new KinematicGameObject(objectConstructor);
            case GHOST:
                return new GhostGameObject(objectConstructor);
        }
        throw new AssertionError("No way");
    }
}
