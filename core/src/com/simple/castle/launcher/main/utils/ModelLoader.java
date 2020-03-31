package com.simple.castle.launcher.main.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;

public class ModelLoader {
    private static final String MAIN_MODEL = "models/map.g3dj";

    private ModelLoader() {
    }

    public static Model loadModel() {
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        return modelLoader.loadModel(Gdx.files.internal(MAIN_MODEL));
    }
}
