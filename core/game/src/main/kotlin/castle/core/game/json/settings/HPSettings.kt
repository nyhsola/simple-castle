package castle.core.game.json.settings

data class HPSettings(
    val enabled: Boolean = true,
    val amount: Int  = 100,
    val textureName: String = ""
)