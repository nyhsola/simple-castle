package castle.core.game.json.settings

data class AttackSettings(
    val enabled: Boolean = true,
    val attackFrom: Int = 0,
    val attackTo: Int = 0,
    val attackSpeed: Float = 0f,
    val scanRange: Float = 0f
)