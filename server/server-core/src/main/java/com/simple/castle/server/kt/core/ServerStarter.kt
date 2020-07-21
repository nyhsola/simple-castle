package com.simple.castle.server.kt.core

import com.badlogic.gdx.Gdx
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ServerStarter(private val serverListener: ServerListener) {
    private val serverListenerService = Executors.newSingleThreadExecutor()
    fun start() {
        serverListenerService.submit(serverListener)
    }

    fun stop() {
        Gdx.app.log("ServerStarter", "Waiting for shutdown listener")
        try {
            serverListener.stop()
            serverListener.dispose()
            serverListenerService.shutdown()
            if (!serverListenerService.awaitTermination(5, TimeUnit.SECONDS)) {
                serverListenerService.shutdownNow()
            }
        } catch (e: InterruptedException) {
            Gdx.app.log("ServerStarter", e.message)
        }
        Gdx.app.log("ServerStarter", "Server was stopped")
    }

}