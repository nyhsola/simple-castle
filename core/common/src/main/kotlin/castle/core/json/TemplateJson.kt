package castle.core.json

import castle.core.json.settings.PhysicSettings
import castle.core.json.settings.RenderSettings

data class TemplateJson(
        val templateName: String = "",
        val renderSettings: RenderSettings = RenderSettings(false),
        val physicSettings: PhysicSettings = PhysicSettings(false)
)