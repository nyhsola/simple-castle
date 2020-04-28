package com.simple.castle.listener;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import javafx.util.Pair;

import java.util.function.Predicate;

public interface CollisionEvent {
    Predicate<Pair<btCollisionObject, btCollisionObject>> getEventFilter();

    void collisionEvent(btCollisionObject object1, btCollisionObject object2);
}
