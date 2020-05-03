package com.simple.castle.object.unit.absunit;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.object.constructors.ObjectConstructor;

public class ActiveGameObject extends AbstractGameObject {
    public ActiveGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor);

        this.body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.body.setContactCallbackFlag(OBJECT_FLAG);
        this.body.setContactCallbackFilter(GROUND_FLAG);
    }
}
