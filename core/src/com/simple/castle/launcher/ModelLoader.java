package com.simple.castle.launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;

public class ModelLoader {
    private static final String MODELS_PLANE_G_3_DJ = "models/data.g3dj";

    public static Model loadModel() {
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        return modelLoader.loadModel(Gdx.files.internal(MODELS_PLANE_G_3_DJ));
    }
}
