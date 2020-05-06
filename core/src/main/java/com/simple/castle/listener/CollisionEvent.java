package com.simple.castle.listener;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public interface CollisionEvent {
    void collisionEvent(btCollisionObject object1, btCollisionObject object2);
}
