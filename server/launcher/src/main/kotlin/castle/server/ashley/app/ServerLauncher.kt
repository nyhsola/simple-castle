package castle.server.ashley.app

import castle.core.common.creator.GUIConfigImpl
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.physics.bullet.Bullet

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val newWidth = (Lwjgl3ApplicationConfiguration.getDisplayMode().width * 0.9).toInt()
        val newHeight = (Lwjgl3ApplicationConfiguration.getDisplayMode().height * 0.9).toInt()
        val conf = Lwjgl3ApplicationConfiguration().apply {
            setWindowedMode(newWidth, newHeight)
        }
        Bullet.init(false, false)
        Lwjgl3Application(ServerGame(GUIConfigImpl()), conf)
        Runtime.getRuntime()
            .addShutdownHook(object : Thread() {
                override fun run() {
                    Gdx.app.postRunnable(Gdx.app::exit)
                }
            })
    }
}