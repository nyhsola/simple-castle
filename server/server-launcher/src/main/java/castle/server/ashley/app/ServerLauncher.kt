package castle.server.ashley.app

import castle.server.ashley.app.creator.GUICreatorImpl
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.physics.bullet.Bullet

object ServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val newWidth = (LwjglApplicationConfiguration.getDesktopDisplayMode().width * 0.9).toInt()
        val newHeight = (LwjglApplicationConfiguration.getDesktopDisplayMode().height * 0.9).toInt()
        val conf = LwjglApplicationConfiguration().apply {
            fullscreen = false
            undecorated = true
            width = newWidth
            height = newHeight
        }

        Bullet.init(false, false)

        LwjglApplication(ServerGame(GUICreatorImpl()), conf)

        Runtime.getRuntime()
            .addShutdownHook(object : Thread() {
                override fun run() {
                    Gdx.app.postRunnable(Gdx.app::exit)
                }
            })
    }
}