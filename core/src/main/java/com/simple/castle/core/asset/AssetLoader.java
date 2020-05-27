package com.simple.castle.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;

public final class AssetLoader {
    private static final G3dModelLoader LOADER = new G3dModelLoader(new JsonReader());

    public Model loadModel() {
        return LOADER.loadModel(Gdx.files.internal("models/map.g3dj"));
    }
}
