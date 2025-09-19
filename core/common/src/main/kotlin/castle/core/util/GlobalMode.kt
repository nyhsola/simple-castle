package castle.core.util

import java.util.*

interface GlobalMode {
    companion object {
        val debugMode = getProp("debugMode").toBoolean()
        val disableCounter = getProp("disableCounter").toBoolean()

        private fun getProp(propertyName: String): String {
            val props = GlobalMode::class.java.classLoader.getResourceAsStream("game.properties").use {
                Properties().apply { load(it) }
            }
            return (props.getProperty(propertyName) as String)
        }
    }
}