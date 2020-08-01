package castle.server.ashley.screen

import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.SceneObjectJson
import com.badlogic.gdx.graphics.g3d.Model

class ConstructorManager(sceneObjectsJson: List<SceneObjectJson>, model: Model) {
    val constructorMap: Map<String, Constructor> = sceneObjectsJson
            .map { sceneObjectJson ->
                Constructor(model,
                        sceneObjectJson.nodePattern,
                        sceneObjectJson.interact,
                        sceneObjectJson.mass,
                        sceneObjectJson.shape,
                        sceneObjectJson.instantiate,
                        sceneObjectJson.hide)
            }
            .associateBy(keySelector = { constructor -> constructor.nodeName }, valueTransform = { constructor -> constructor })
            .toMap()
}