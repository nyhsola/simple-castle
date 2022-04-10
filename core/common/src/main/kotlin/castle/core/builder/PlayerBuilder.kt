package castle.core.builder

import castle.core.`object`.CommonEntity
import castle.core.component.PositionComponent
import castle.core.component.SideComponent
import castle.core.component.UnitComponent
import castle.core.json.PlayerJson
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

    fun buildUnits(playerJson: PlayerJson): List<CommonEntity> {
        if (!playerJson.pathSettings.enabled) {
            return emptyList()
        }
        return playerJson.pathSettings.paths.map { path -> buildInternal(playerJson.playerName, "warrior", path[0]).also { initPath(it, path) } }
    }

    private fun buildInternal(playerName: String, unitStr: String, spawnStr: String): CommonEntity {
        val unitJson = gameResources.units.getValue(unitStr)
        val unit = unitBuilder.build(unitJson)
        val spawnPoint = environmentInitService.neutralUnits.getValue(spawnStr)
        initSpawnPoint(unit, spawnPoint)
        unit.add(SideComponent(playerName))
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