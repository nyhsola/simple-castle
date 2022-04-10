package castle.core.json

import castle.core.json.settings.PathSettings
import castle.core.json.settings.StartupSettings

data class PlayerJson(
        val playerName: String = "",
        val startupSettings: StartupSettings = StartupSettings(false),
        val pathSettings: PathSettings = PathSettings(false)
)