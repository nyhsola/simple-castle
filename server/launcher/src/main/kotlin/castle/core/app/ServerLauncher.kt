package castle.core.app

import castle.core.game.ServerGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import java.util.*

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val width = (Lwjgl3ApplicationConfiguration.getDisplayMode().width * 0.999f).toInt()
        val height = (Lwjgl3ApplicationConfiguration.getDisplayMode().height * 0.9f).toInt()
        Lwjgl3Application(ServerGame(), Lwjgl3ApplicationConfiguration().apply {
            setWindowedMode(width, height)
        })
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                Gdx.app.postRunnable(Gdx.app::exit)
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getProp(key: String): T {
        val props = javaClass.classLoader.getResourceAsStream("game.properties").use { Properties().apply { load(it) } }
        return (props.getProperty(key) as T) ?: throw RuntimeException("could not find property $key")
    }
}