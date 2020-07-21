package com.simple.castle.core.kt.api

import com.badlogic.gdx.net.Socket
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class NetTool(socket: Socket) {
    private val inputStream: ObjectInputStream?
    private val outputStream: ObjectOutputStream?
    fun <T> read(): T? {
        var readObject: T? = null
        try {
            if (inputStream != null) {
                readObject = inputStream.readObject() as T
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        } catch (exception: ClassNotFoundException) {
            exception.printStackTrace()
        }
        return readObject
    }

    fun <T> write(`object`: T) {
        try {
            outputStream?.writeObject(`object`)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    init {
        var inputStream: ObjectInputStream? = null
        var outputStream: ObjectOutputStream? = null
        try {
            outputStream = ObjectOutputStream(socket.outputStream)
            inputStream = ObjectInputStream(socket.inputStream)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        this.inputStream = inputStream
        this.outputStream = outputStream
    }
}