package castle.server.ashley.app

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.physics.bullet.Bullet

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Bullet.init(false, false)
        Lwjgl3Application(ServerGame(), Lwjgl3ApplicationConfiguration().apply {
            setWindowedMode(
                (Lwjgl3ApplicationConfiguration.getDisplayMode().width * 0.9).toInt(),
                (Lwjgl3ApplicationConfiguration.getDisplayMode().height * 0.9).toInt()
            )
        })
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                Gdx.app.postRunnable(Gdx.app::exit)
            }
        })
    }
}