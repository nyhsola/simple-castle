package com.simple.castle.server.kt.manager

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.server.kt.composition.BaseObject
import com.simple.castle.server.kt.composition.Constructor
import com.simple.castle.server.kt.loader.json.SceneObjectsJson

class SceneManager(sceneObjectsJson: SceneObjectsJson,
                   model: Model?) : Disposable {
    private val constructorMap: Map<String, Constructor>
    private val baseObjectMap: Map<String, BaseObject>
    val drawables: List<ModelInstance>

    val all: Collection<BaseObject>
        get() = baseObjectMap.values

    init {
        constructorMap = sceneObjectsJson.sceneObjectsJson
                ?.map { sceneObjectJson ->
                    Constructor(model!!,
                            sceneObjectJson!!.nodePattern!!,
                            sceneObjectJson.interact!!,
                            sceneObjectJson.shape!!,
                            sceneObjectJson.instantiate!!,
                            sceneObjectJson.hide!!)
                }
                ?.associateBy(keySelector = { constructor -> constructor.id }, valueTransform = { constructor -> constructor })!!

        baseObjectMap = constructorMap.entries
                .filter { entry -> entry.value.instantiate }
                .associateBy(keySelector = { entry -> entry.key }, valueTransform = { entry -> BaseObject(entry.value) })

        drawables = baseObjectMap.values
                .filter { baseObject -> !baseObject.hide!! }
                .map { baseObject -> baseObject.modelInstance!! }

    }

    fun getObject(id: String?): BaseObject? {
        return baseObjectMap[id]
    }

    override fun dispose() {
        baseObjectMap.values.forEach { obj: BaseObject -> obj.dispose() }
    }
}