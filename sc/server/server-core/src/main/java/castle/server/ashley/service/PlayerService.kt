package castle.server.ashley.service

import castle.server.ashley.component.PlayerComponent
import castle.server.ashley.physic.Constructor
import castle.server.ashley.utils.ResourceManager
import castle.server.ashley.utils.json.PlayerJson
import castle.server.ashley.utils.json.SceneObjectJson
import com.badlogic.ashley.core.Engine

class PlayerService(resourceManager: ResourceManager) {
    private val playersJson: List<PlayerJson> = resourceManager.players
    private val sceneObjectsJson: List<SceneObjectJson> = resourceManager.sceneObjectsJson
    private val constructorMap: Map<String, Constructor> = sceneObjectsJson
        .map { sceneObjectJson ->
            Constructor(
                resourceManager.model,
                sceneObjectJson.nodes,
                sceneObjectJson.collisionFlag,
                sceneObjectJson.collisionFilterGroup,
                sceneObjectJson.collisionFilterMask,
                sceneObjectJson.mass,
                sceneObjectJson.shape,
                sceneObjectJson.instantiate,
                sceneObjectJson.hide,
                sceneObjectJson.armature,
                sceneObjectJson.animation
            )
        }
        .associateBy(keySelector = { constructor -> constructor.node }, valueTransform = { constructor -> constructor })
        .toMap()

    fun instantiateStartPositions(engine: Engine) {
        constructorMap
            .asIterable()
            .filter { entry -> entry.value.instantiate }
            .forEach { entry -> engine.addEntity(entry.value.instantiate(engine)) }
        playersJson.forEach {
            val entity = engine.createEntity()
            val playerComponent = PlayerComponent.createComponent(engine, it)
            entity.add(playerComponent)
            engine.addEntity(entity)
        }
    }
}