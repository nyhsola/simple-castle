package castle.core.app

import castle.core.game.ServerGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val width = Lwjgl3ApplicationConfiguration.getDisplayMode().width
        val height = Lwjgl3ApplicationConfiguration.getDisplayMode().height
        Lwjgl3Application(ServerGame(), Lwjgl3ApplicationConfiguration().apply {
            setDecorated(false)
            setWindowedMode(width, height)
        })
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                Gdx.app.postRunnable(Gdx.app::exit)
            }
        })
    }
}