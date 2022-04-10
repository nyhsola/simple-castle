package castle.core.json.settings

data class StartupSettings(
        val enabled: Boolean = true,
        val units: HashMap<String, String> = HashMap()
)