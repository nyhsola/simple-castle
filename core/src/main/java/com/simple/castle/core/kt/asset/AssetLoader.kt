package com.simple.castle.core.kt.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.utils.JsonReader

class AssetLoader {
    fun loadModel(): Model {
        return LOADER.loadModel(Gdx.files.internal("models/map.g3dj"))
    }

    companion object {
        private val LOADER = G3dModelLoader(JsonReader())
    }
}