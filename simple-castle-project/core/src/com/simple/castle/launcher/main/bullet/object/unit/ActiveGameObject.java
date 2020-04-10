package com.simple.castle.launcher.main.bullet.object.unit;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.launcher.main.bullet.object.AbstractGameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;

public class ActiveGameObject extends AbstractGameObject {
    public ActiveGameObject(GameObjectConstructor gameObjectConstructor) {
        super(gameObjectConstructor);

        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.body.setContactCallbackFlag(AbstractGameObject.OBJECT_FLAG);
        this.body.setContactCallbackFilter(AbstractGameObject.GROUND_FLAG);
    }
}
