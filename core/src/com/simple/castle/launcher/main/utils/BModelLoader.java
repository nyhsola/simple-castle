package com.simple.castle.launcher.main.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;

public class BModelLoader {
    private static final String MODELS_PLANE_G_3_DJ = "models/map.g3dj";

    public static Model loadModel() {
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        return modelLoader.loadModel(Gdx.files.internal(MODELS_PLANE_G_3_DJ));
    }
}
