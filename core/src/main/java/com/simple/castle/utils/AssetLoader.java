package com.simple.castle.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;

public final class AssetLoader {
    private static final String MAIN_MODEL = "models/map.g3dj";
    private static final String UI_UISKIN_JSON = "ui/uiskin.json";

    private AssetLoader() {
    }

    public static Model loadModel() {
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        return modelLoader.loadModel(Gdx.files.internal(MAIN_MODEL));
    }

    public static Skin loadSkin() {
        return new Skin(Gdx.files.internal(UI_UISKIN_JSON));
    }
}
