package castle.core.game.json

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PositionComponent
import castle.core.game.`object`.GameContext
import castle.core.game.component.PathComponent
import castle.core.game.component.PlayerComponent
import castle.core.game.json.settings.PathSettings
import castle.core.game.json.settings.StartupSettings

data class PlayerJson(
    val playerName: String = "",
    val startupSettings: StartupSettings = StartupSettings(false),
    val pathSettings: PathSettings = PathSettings(false)
) {
    fun buildStartup(gameContext: GameContext): List<CommonEntity> {
        val resourceService = gameContext.getResourceService()
        return startupSettings.units
            .map { resourceService.units.getValue(it.key).buildUnit(resourceService, it.value) }
            .map { initPlayerUnit(it) }
    }

    fun buildUnit(gameContext: GameContext): CommonEntity {
        val resourceService = gameContext.getResourceService()
        val unit = resourceService.units.getValue("warrior").buildUnit(resourceService)
        if (pathSettings.enabled) {
            initSpawnPoint(gameContext, unit)
            unit.add(initPath())
        }
        return initPlayerUnit(unit)
    }

    private fun initPlayerUnit(entity: CommonEntity) : CommonEntity {
        if (playerName.isNotEmpty()) {
            entity.add(PlayerComponent(playerName))
        }
        return entity
    }

    private fun initSpawnPoint(gameContext: GameContext, unit: CommonEntity) {
        val spawnPoint = gameContext.gameEnvironment.environment.getValue(pathSettings.paths[0][0])
        val matrix4Unit = PositionComponent.mapper.get(unit).matrix4
        val matrix4Spawn = PositionComponent.mapper.get(spawnPoint).matrix4
        matrix4Unit.set(matrix4Spawn)
    }

    private fun initPath(): PathComponent {
        return PathComponent(pathSettings.paths[0])
    }
}