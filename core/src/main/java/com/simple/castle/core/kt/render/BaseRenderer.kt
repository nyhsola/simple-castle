package com.simple.castle.core.kt.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance

class BaseRenderer(private val modelBatch: ModelBatch) {
    fun render(camera: Camera, baseObjectList: Collection<ModelInstance>, baseEnvironment: BaseEnvironment) {
        Gdx.gl.apply {
            glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
            glClearColor(0.3f, 0.3f, 0.3f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        }

        modelBatch.begin(camera)
        modelBatch.render(baseObjectList, baseEnvironment.environment)
        modelBatch.end()
    }

    fun dispose() {
        modelBatch.dispose()
    }

}