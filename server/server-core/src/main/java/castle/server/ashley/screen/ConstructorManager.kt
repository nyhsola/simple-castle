package castle.server.ashley.screen

import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.SceneObjectJson
import com.badlogic.gdx.graphics.g3d.Model

class ConstructorManager(sceneObjectsJson: List<SceneObjectJson>, model: Model) {
    val constructorMap: Map<String, Constructor> = sceneObjectsJson
            .map { sceneObjectJson ->
                Constructor(model,
                        sceneObjectJson.nodes,
                        sceneObjectJson.interact,
                        sceneObjectJson.mass,
                        sceneObjectJson.shape,
                        sceneObjectJson.instantiate,
                        sceneObjectJson.hide,
                        sceneObjectJson.animation)
            }
            .associateBy(keySelector = { constructor -> constructor.nodesStr }, valueTransform = { constructor -> constructor })
            .toMap()
}