package com.simple.castle.object.constructors.tool;

import com.simple.castle.object.constructors.ObjectConstructor;
import com.simple.castle.object.unit.absunit.AbstractGameObject;
import com.simple.castle.object.unit.absunit.ActiveGameObject;
import com.simple.castle.object.unit.absunit.GhostGameObject;
import com.simple.castle.object.unit.absunit.KinematicGameObject;

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
