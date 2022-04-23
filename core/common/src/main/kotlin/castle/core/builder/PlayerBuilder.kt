package castle.core.builder

import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.json.PlayerJson
import castle.core.`object`.CommonEntity
import castle.core.service.EnvironmentInitService
import castle.core.service.GameResources

class PlayerBuilder(
    private val gameResources: GameResources,
    private val environmentInitService: EnvironmentInitService,
    private val unitBuilder: UnitBuilder
) {
    fun buildBuildings(playerJson: PlayerJson): List<CommonEntity> {
        return playerJson.startupSettings.units.map { buildInternal(playerJson.playerName, it.key, it.value) }
    }

    fun buildUnit(playerJson: PlayerJson, lineNumber: Int): CommonEntity {
        val line = playerJson.pathSettings.paths[lineNumber]
        val spawnPoint = line[0]
        return buildInternal(playerJson.playerName, "warrior", spawnPoint).also { initPath(it, line) }
    }

    private fun buildInternal(playerName: String, unitStr: String, spawnStr: String): CommonEntity {
        val unitJson = gameResources.units.getValue(unitStr)
        val unit = unitBuilder.build(unitJson)
        val spawnPoint = environmentInitService.neutralUnits.getValue(spawnStr)
        UnitComponent.mapper.get(unit).playerName = playerName
        initSpawnPoint(unit, spawnPoint)
        return unit
    }

    private fun initPath(entity: CommonEntity, path: List<String>) {
        UnitComponent.mapper.get(entity).path.addAll(path)
    }

    private fun initSpawnPoint(unit: CommonEntity, spawnPoint: CommonEntity) {
        val matrix4Unit = PositionComponent.mapper.get(unit).matrix4
        val matrix4Spawn = PositionComponent.mapper.get(spawnPoint).matrix4
        matrix4Unit.set(matrix4Spawn)
    }
}