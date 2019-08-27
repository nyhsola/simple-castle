package com.simple.castle.server.kt.physic

internal interface Flags {
    companion object {
        const val GROUND_FLAG = (1 shl 8.toShort().toInt()).toShort()
        const val OBJECT_FLAG = (1 shl 9.toShort().toInt()).toShort()
        const val ALL_FLAG: Short = -1
    }
}