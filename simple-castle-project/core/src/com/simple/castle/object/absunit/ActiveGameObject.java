package com.simple.castle.object.absunit;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.object.GameObjectConstructor;

public class ActiveGameObject extends AbstractGameObject {
    public ActiveGameObject(GameObjectConstructor gameObjectConstructor) {
        super(gameObjectConstructor);

        this.body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.body.setContactCallbackFlag(OBJECT_FLAG);
        this.body.setContactCallbackFilter(GROUND_FLAG);
    }
}
