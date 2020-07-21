package com.simple.castle.server.kt.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.utils.Disposable
import com.simple.castle.core.api.NetTool
import com.simple.castle.core.api.ServerCommands
import com.simple.castle.server.kt.game.ServerGame

class UserWorker(private val socket: Socket, private val serverGame: ServerGame?) : Runnable, Disposable {
    override fun run() {
        Gdx.app.log("UserWorker", "User connected, starting transfer data")
        val netTool = NetTool(socket)
        while (!Thread.currentThread().isInterrupted && socket.isConnected) {
//            process(netTool);
        }
    }

    private fun process(netTool: NetTool) {
        val read = netTool.read<Byte>()
        if (ServerCommands.GET_CURRENT_POSITIONS == read) {
//            ServerState state = serverGame.getState();
//            netTool.write(state);
        }
    }

    override fun dispose() {
        socket.dispose()
    }

}