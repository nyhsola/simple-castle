package com.simple.castle.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.IOException;
import java.io.ObjectInputStream;

public class GameLauncher extends Game {

    Socket socket;
    ObjectInputStream objectInputStream;

    @Override
    public void create() {
        SocketHints socketHints = new SocketHints();
        // Socket will time our in 4 seconds
        socketHints.connectTimeout = 4000;

        System.out.println("Creating");
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "127.0.0.1", 9090, socketHints);
        System.out.println("Accepted " + socket.isConnected());

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException exception) {
            System.out.println("fail");
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        try {
            Gdx.app.log("tag", String.valueOf(objectInputStream.readObject()));
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}