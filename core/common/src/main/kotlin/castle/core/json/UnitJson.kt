package castle.core.json

import castle.core.json.settings.HPSettings
import castle.core.json.settings.UnitSettings

data class UnitJson(
        val unitName: String = "",
        val templateName: String = "",
        val node: String = "",
        val unitSettings: UnitSettings = UnitSettings(false),
        val hpSettings: HPSettings = HPSettings(false)
)