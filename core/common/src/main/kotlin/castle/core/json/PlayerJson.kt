package castle.core.json

data class PlayerJson(
        val playerName: String = "",
        val paths: List<List<String>> = emptyList(),
        val units: HashMap<String, String> = HashMap()
)