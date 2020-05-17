package com.simple.castle.game.core.object.unit.basic;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.server.game.core.object.unit.add.ObjectConstructor;

public class ActiveGameObject extends AbstractGameObject {
    public ActiveGameObject(ObjectConstructor objectConstructor) {
        super(objectConstructor);

        this.body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.body.setContactCallbackFlag(OBJECT_FLAG);
        this.body.setContactCallbackFilter(GROUND_FLAG);
    }
}
