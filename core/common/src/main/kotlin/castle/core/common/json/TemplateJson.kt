package castle.core.common.json

import castle.core.common.json.settings.PhysicSettings
import castle.core.common.json.settings.RenderSettings

data class TemplateJson(
    val templateName: String = "",
    val nodeName: String = "",
    val renderSettings: RenderSettings = RenderSettings(false),
    val physicSettings: PhysicSettings = PhysicSettings(false)
)