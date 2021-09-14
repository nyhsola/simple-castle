package castle.core.game.json.settings

data class MoveSettings(
    val enabled: Boolean = true,
    val speedLinear: Float = 0.0f,
    val speedAngular: Float = 0.0f
)