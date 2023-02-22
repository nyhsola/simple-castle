package castle.core.util

import java.util.*

interface GlobalMode {
    companion object {
        val debugMode = getProp().toBoolean()
        private fun getProp(): String {
            val props = GlobalMode::class.java.classLoader.getResourceAsStream("game.properties").use {
                Properties().apply { load(it) }
            }
            return (props.getProperty("debugMode") as String)
        }
    }
}