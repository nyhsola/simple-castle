package castle.core.game.json

import castle.core.game.json.settings.PathSettings
import castle.core.game.json.settings.StartupSettings

data class PlayerJson(
    val playerName: String = "",
    val startupSettings: StartupSettings = StartupSettings(false),
    val pathSettings: PathSettings = PathSettings(false)
)