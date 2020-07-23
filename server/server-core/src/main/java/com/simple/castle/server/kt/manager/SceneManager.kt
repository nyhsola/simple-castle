package com.simple.castle.server.kt.manager

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.server.kt.composition.BaseObject
import com.simple.castle.server.kt.composition.Constructor
import com.simple.castle.server.kt.loader.json.SceneObjectJson

class SceneManager(sceneObjectsJson: List<SceneObjectJson>,
                   model: Model) : Disposable {
    private val constructorMap: MutableMap<String, Constructor>
    private val baseObjectMap: MutableMap<String, BaseObject>
    val drawables: MutableSet<ModelInstance>

    val all: List<BaseObject>
        get() = baseObjectMap.values.toList()

    init {
        constructorMap = sceneObjectsJson
                .map { sceneObjectJson ->
                    Constructor(model,
                            sceneObjectJson.nodePattern,
                            sceneObjectJson.interact,
                            sceneObjectJson.mass,
                            sceneObjectJson.shape,
                            sceneObjectJson.instantiate,
                            sceneObjectJson.hide)
                }
                .associateBy(keySelector = { constructor -> constructor.id }, valueTransform = { constructor -> constructor })
                .toMutableMap()

        baseObjectMap = constructorMap.entries
                .filter { entry -> entry.value.instantiate }
                .associateBy(keySelector = { entry -> entry.key }, valueTransform = { entry -> BaseObject(entry.value) })
                .toMutableMap()

        drawables = baseObjectMap.values
                .filter { baseObject -> !baseObject.hide }
                .map { baseObject -> baseObject.modelInstance }
                .toMutableSet()
    }

    fun addObjects(drawables: List<BaseObject>) {
        drawables.forEach { drawable -> addObjects(drawable) }
    }

    fun addObjects(drawable: BaseObject) {
        baseObjectMap[drawable.id]
        if (!drawable.hide) drawables.add(drawable.modelInstance)
    }

    fun getObject(id: String): BaseObject? {
        return baseObjectMap[id]
    }

    fun getConstructor(id: String): Constructor? {
        return constructorMap[id]
    }

    override fun dispose() {
        baseObjectMap.values.forEach { obj: BaseObject -> obj.dispose() }
    }
}