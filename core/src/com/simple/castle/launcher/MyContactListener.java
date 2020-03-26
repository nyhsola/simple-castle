package com.simple.castle.launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class MyContactListener extends ContactListener {
    @Override
    public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
        Gdx.app.log("", "Collision!");
        super.onContactStarted(colObj0, colObj1);
    }
}
