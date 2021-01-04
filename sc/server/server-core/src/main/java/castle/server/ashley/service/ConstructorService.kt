package castle.server.ashley.service

import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.json.SceneObjectJson
import com.badlogic.gdx.graphics.g3d.Model

class ConstructorService(sceneObjectsJson: List<SceneObjectJson>, model: Model) {
    val constructorMap: Map<String, Constructor> = sceneObjectsJson
        .map { sceneObjectJson ->
            Constructor(
                model,
                sceneObjectJson.nodes,
                sceneObjectJson.collisionFlag,
                sceneObjectJson.collisionFilterGroup,
                sceneObjectJson.collisionFilterMask,
                sceneObjectJson.mass,
                sceneObjectJson.shape,
                sceneObjectJson.instantiate,
                        sceneObjectJson.hide,
                        sceneObjectJson.armature,
                        sceneObjectJson.animation)
            }
            .associateBy(keySelector = { constructor -> constructor.node }, valueTransform = { constructor -> constructor })
            .toMap()
}