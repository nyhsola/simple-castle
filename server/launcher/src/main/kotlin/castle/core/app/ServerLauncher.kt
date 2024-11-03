package castle.core.app

import castle.core.game.ServerGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val pcWidth = Lwjgl3ApplicationConfiguration.getDisplayMode().width
        val pcHeight = Lwjgl3ApplicationConfiguration.getDisplayMode().height
        val width = (pcWidth * 0.95).toInt()
        val height = (pcHeight * 0.95).toInt()
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