package castle.server.ashley.app

import castle.server.ashley.creator.GUICreatorImpl
import castle.server.ashley.creator.GUICreatorMock
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import org.mockito.Mockito

class ServerApplication(private var isGUI: Boolean = true, private var isServer: Boolean = false, private var conf: LwjglApplicationConfiguration? = null) {

    fun run() {
        val application: Application
        if (isGUI) {
            val guiCreator = GUICreatorImpl()
            application = LwjglApplication(ServerMain(guiCreator), conf)
        } else {
            val guiCreator = GUICreatorMock()
            Gdx.gl = Mockito.mock(GL20::class.java)
            Gdx.gl20 = Mockito.mock(GL20::class.java)
            Gdx.gl30 = Mockito.mock(GL30::class.java)
            application = HeadlessApplication(ServerMain(guiCreator))
        }

        if (isServer) {
//            val serverListener = ServerListener(game)
//            val serverStarter = ServerStarter(serverListener)
//            serverStarter.start()
            application.addLifecycleListener(object : LifecycleListener {
                override fun pause() {}
                override fun resume() {}
                override fun dispose() {
//                    serverStarter.stop()
                }
            })
        }

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                Gdx.app.postRunnable(Gdx.app::exit)
            }
        })
    }

}