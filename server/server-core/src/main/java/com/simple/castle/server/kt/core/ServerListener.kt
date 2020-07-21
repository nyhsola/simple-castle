package com.simple.castle.server.kt.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.ServerSocket
import com.badlogic.gdx.net.ServerSocketHints
import com.badlogic.gdx.net.SocketHints
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.GdxRuntimeException
import com.simple.castle.server.kt.game.ServerGame
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ServerListener(private val serverGame: ServerGame?) : Runnable, Disposable {
    private val userWorkersService = Executors.newFixedThreadPool(MAX_USERS)
    private val workers = Collections.synchronizedSet(HashSet<UserWorker>())
    private val serverSocketHints = ServerSocketHints()
    private val socketHints = SocketHints()
    private var serverSocket: ServerSocket? = null
    private var isRunning = false

    override fun run() {
        Gdx.app.log("ServerListener", "Server going to wait for clients")
        isRunning = true
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9090, serverSocketHints)
        while (isRunning && !Thread.currentThread().isInterrupted) {
            Gdx.app.log("ServerListener", "Wait for next connection...")
            try {
                val socket = serverSocket!!.accept(socketHints)
                val worker = UserWorker(socket, serverGame)
                workers.add(worker)
                userWorkersService.submit(worker)
            } catch (exception: GdxRuntimeException) {
                Gdx.app.log("ServerListener", "Socket accept error")
            }
        }
        Gdx.app.log("ServerListener", "End of listening")
    }

    fun stop() {
        isRunning = false
        try {
            Gdx.app.log("ServerListener", "Server going to shutdown user workers")
            userWorkersService.shutdown()
            if (!userWorkersService.awaitTermination(1, TimeUnit.SECONDS)) {
                userWorkersService.shutdownNow()
            }
            for (userWorker in workers) {
                userWorker.dispose()
            }
        } catch (e: InterruptedException) {
            Gdx.app.error("ServerListener", "Interrupted: " + e.message)
        }
    }

    override fun dispose() {
        if (serverSocket != null) {
            Gdx.app.log("ServerListener", "Server socket disposed")
            serverSocket!!.dispose()
        }
    }

    companion object {
        private const val MAX_USERS = 4
    }

    init {
        serverSocketHints.acceptTimeout = 0
    }
}