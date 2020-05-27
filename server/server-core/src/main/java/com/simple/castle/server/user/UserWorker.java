package com.simple.castle.server.user;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.core.ServerState;
import com.simple.castle.core.api.NetTool;
import com.simple.castle.core.api.ServerCommands;
import com.simple.castle.server.game.ServerGame;

public class UserWorker implements Runnable, Disposable {
    private final Socket socket;
    private final ServerGame serverGame;

    public UserWorker(Socket socket, ServerGame serverGame) {
        this.socket = socket;
        this.serverGame = serverGame;
    }

    @Override
    public void run() {
        Gdx.app.log("UserWorker", "User connected, starting transfer data");
        NetTool netTool = new NetTool(socket);
        while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
            process(netTool);
        }
    }

    private void process(NetTool netTool) {
        Byte read = netTool.read();
        if (ServerCommands.GET_CURRENT_POSITIONS.equals(read)) {
            ServerState state = serverGame.getState();
            netTool.write(state);
        }
    }

    @Override
    public void dispose() {
        socket.dispose();
    }
}
