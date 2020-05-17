package com.simple.castle.server.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.JsonReader;

public class GameServer implements ApplicationListener {

    @Override
    public void create() {
        Bullet.init();

        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        ModelData modelData = modelLoader.parseModel(Gdx.files.internal("models/map.g3dj"));

//        Bullet.obtainStaticShape()
//        Bullet.obtainStaticShape()
        modelLoader.loadModel(Gdx.files.internal("models/map.g3dj"));
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