package castle.core.json.settings

data class PathSettings(
        val enabled: Boolean = true,
        val paths: List<List<String>> = emptyList()
)