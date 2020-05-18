package com.simple.castle.server.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.physics.bullet.Bullet;

public final class GameServer implements ApplicationListener {
    @Override
    public void create() {
        Bullet.init();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}