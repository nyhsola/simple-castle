package castle.core.builder

import castle.core.component.PositionComponent
import castle.core.component.UnitComponent
import castle.core.json.PlayerJson
import castle.core.service.EnvironmentService
import castle.core.service.GameResources
import com.badlogic.ashley.core.Entity

class PlayerBuilder(
    private val gameResources: GameResources,
    private val environmentService: EnvironmentService,
    private val unitBuilder: UnitBuilder
) {
    fun buildBuildings(playerJson: PlayerJson): List<Entity> {
        return playerJson.units.map { buildInternal(playerJson.playerName, it.key, it.value) }
    }

    fun buildUnit(playerJson: PlayerJson, lineNumber: Int): Entity {
        val line = playerJson.paths[lineNumber]
        val spawnPoint = line[0]
        return buildInternal(playerJson.playerName, "warrior", spawnPoint).also { initPath(it, line) }
    }

    private fun buildInternal(playerName: String, unitStr: String, spawnStr: String): Entity {
        val unitJson = gameResources.units.getValue(unitStr)
        val unit = unitBuilder.build(unitJson)
        val spawnPoint = environmentService.neutralUnits.getValue(spawnStr)
        UnitComponent.mapper.get(unit).playerName = playerName
        initSpawnPoint(unit, spawnPoint)
        return unit
    }

    private fun initPath(entity: Entity, path: List<String>) {
        UnitComponent.mapper.get(entity).path.addAll(path)
    }

    private fun initSpawnPoint(unit: Entity, spawnPoint: Entity) {
        val matrix4Unit = PositionComponent.mapper.get(unit).matrix4
        val matrix4Spawn = PositionComponent.mapper.get(spawnPoint).matrix4
        matrix4Unit.set(matrix4Spawn)
    }
}