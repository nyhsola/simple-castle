package castle.core.app

import castle.core.game.ServerGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Lwjgl3Application(ServerGame(), Lwjgl3ApplicationConfiguration().apply {
            setResizable(true)
//            setDecorated(false)
            setWindowedMode(
                    (Lwjgl3ApplicationConfiguration.getDisplayMode().width * 0.999f).toInt(),
                    (Lwjgl3ApplicationConfiguration.getDisplayMode().height * 0.9f).toInt()
            )
        })
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                Gdx.app.postRunnable(Gdx.app::exit)
            }
        })
    }
}