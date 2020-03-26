package com.simple.castle.launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public class MyContactListener extends ContactListener {
    @Override
    public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
        Gdx.app.log("TAG", "Collision!");
        super.onContactStarted(colObj0, colObj1);
    }

    @Override
    public boolean onContactAdded(btManifoldPoint cp, btCollisionObject colObj0, int partId0, int index0, boolean match0, btCollisionObject colObj1, int partId1, int index1, boolean match1) {
        Gdx.app.log("TAG", "Collision!");
        return super.onContactAdded(cp, colObj0, partId0, index0, match0, colObj1, partId1, index1, match1);
    }
}
