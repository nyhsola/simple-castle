package com.simple.castle.server.composition.add;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.simple.castle.server.physic.unit.ActiveObject;
import com.simple.castle.server.physic.unit.KinematicObject;
import com.simple.castle.server.physic.unit.PhysicObject;

import java.util.function.Function;

public enum InteractType {
    ACTIVE(ActiveObject::new),
    KINEMATIC(KinematicObject::new),
    GHOST(KinematicObject::new);

    private final Function<btRigidBody.btRigidBodyConstructionInfo, PhysicObject> function;

    InteractType(Function<btRigidBody.btRigidBodyConstructionInfo, PhysicObject> function) {
        this.function = function;
    }

    public PhysicObject build(btRigidBody.btRigidBodyConstructionInfo info) {
        return function.apply(info);
    }
}
