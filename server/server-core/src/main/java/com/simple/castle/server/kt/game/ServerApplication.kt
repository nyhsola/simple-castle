package com.simple.castle.server.kt.game

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.simple.castle.server.kt.core.ServerListener
import com.simple.castle.server.kt.core.ServerStarter
import org.mockito.Mockito

class ServerApplication(
        private var isGUI: Boolean = true,
        private var isServer: Boolean = false,
        private var gameSupplier: ((() -> ModelBatch) -> ServerGame)? = null,
        private var conf: LwjglApplicationConfiguration? = null) {

    fun run() {
        if (!isGUI) {
            Gdx.gl = Mockito.mock(GL20::class.java)
            Gdx.gl20 = Mockito.mock(GL20::class.java)
            Gdx.gl30 = Mockito.mock(GL30::class.java)
        }

        val game = gameSupplier?.invoke { if (isGUI) ModelBatch() else Mockito.mock(ModelBatch::class.java) }
        val application: Application = if (isGUI) LwjglApplication(game, conf) else HeadlessApplication(game)

        if (isServer) {
            val serverListener = ServerListener(game)
            val serverStarter = ServerStarter(serverListener)
            serverStarter.start()
            application.addLifecycleListener(object : LifecycleListener {
                override fun pause() {}
                override fun resume() {}
                override fun dispose() {
                    serverStarter.stop()
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