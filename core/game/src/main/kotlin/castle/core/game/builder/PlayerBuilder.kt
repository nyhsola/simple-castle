package castle.core.game.builder

import castle.core.common.`object`.CommonEntity
import castle.core.common.component.PositionComponent
import castle.core.game.component.PathComponent
import castle.core.game.component.PlayerComponent
import castle.core.game.component.SideComponent
import castle.core.game.json.PlayerJson
import castle.core.game.service.GameResources
import castle.core.game.service.NeutralInitService

class PlayerBuilder(
    private val gameResources: GameResources,
    private val neutralInitService: NeutralInitService,
    private val unitBuilder: UnitBuilder
) {
    fun buildPlayer(playerJson: PlayerJson): CommonEntity {
        val commonEntity = CommonEntity()
        commonEntity.add(PlayerComponent(playerJson))
        return commonEntity
    }

    fun buildBuildings(playerJson: PlayerJson): List<CommonEntity> {
        return playerJson.startupSettings.units.map { unitBuilder.build(gameResources.units.getValue(it.key), it.value) }
    }

    fun buildUnits(playerJson: PlayerJson): List<CommonEntity> {
        val units: MutableList<CommonEntity> = ArrayList()
        if (playerJson.pathSettings.enabled) {
            for (path in playerJson.pathSettings.paths) {
                val entity = unitBuilder.build(gameResources.units.getValue("warrior"))
                initSpawnPoint(neutralInitService.neutralUnits.getValue(path[0]), entity)
                entity.add(SideComponent(playerJson.playerName))
                entity.add(PathComponent(path))
                units.add(entity)
            }
        }
        return units
    }

    private fun initSpawnPoint(spawnPoint: CommonEntity, unit: CommonEntity) {
        val matrix4Unit = PositionComponent.mapper.get(unit).matrix4
        val matrix4Spawn = PositionComponent.mapper.get(spawnPoint).matrix4
        matrix4Unit.set(matrix4Spawn)
    }
}