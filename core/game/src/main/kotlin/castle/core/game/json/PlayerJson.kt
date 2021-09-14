package castle.core.game.json

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PositionComponent
import castle.core.game.`object`.GameContext
import castle.core.game.component.PathComponent
import castle.core.game.component.PlayerComponent
import castle.core.game.json.settings.PathSettings
import castle.core.game.json.settings.StartupSettings
import castle.core.game.service.GameResourceService

data class PlayerJson(
    val playerName: String = "",
    val startupSettings: StartupSettings = StartupSettings(false),
    val pathSettings: PathSettings = PathSettings(false)
) {
    fun buildStartup(gameResourceService: GameResourceService): List<CommonEntity> {
        return startupSettings.units.map { gameResourceService.units.getValue(it.key).buildUnit(gameResourceService, it.value) }
    }

    fun buildUnit(gameContext: GameContext): CommonEntity {
        val resourceService = gameContext.getResourceService()
        val unit = resourceService.units.getValue("warrior").buildUnit(resourceService)
        if (pathSettings.enabled) {
            initSpawnPoint(gameContext, unit)
            unit.add(initPath())
        }
        if (playerName.isNotEmpty()) {
            unit.add(PlayerComponent(playerName))
        }
        return unit
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