package com.simple.castle.core.api;

import com.badlogic.gdx.net.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NetTool {

    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public NetTool(Socket socket) {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @SuppressWarnings("unchecked")
    public <T> T read() {
        T readObject = null;
        try {
            if (inputStream != null) {
                readObject = (T) inputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return readObject;
    }

    public <T> void write(T object) {
        try {
            if (outputStream != null) {
                outputStream.writeObject(object);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
