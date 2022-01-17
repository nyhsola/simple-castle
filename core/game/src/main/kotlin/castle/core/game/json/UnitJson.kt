package castle.core.game.json

import castle.core.game.json.settings.AttackSettings
import castle.core.game.json.settings.BehaviourSettings
import castle.core.game.json.settings.HPSettings
import castle.core.game.json.settings.MoveSettings

data class UnitJson(
    val unitName: String = "",
    val templateName: String = "",
    val attackSettings: AttackSettings = AttackSettings(false),
    val moveSettings: MoveSettings = MoveSettings(false),
    val hpSettings: HPSettings = HPSettings(false),
    val behaviourSettings: BehaviourSettings = BehaviourSettings(false)
)