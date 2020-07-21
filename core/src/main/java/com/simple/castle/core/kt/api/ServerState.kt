package com.simple.castle.core.kt.api

import java.io.Serializable
import java.util.*

class ServerState(modelSends: List<ModelSend>) : Serializable {
    private val modelSends: MutableList<ModelSend>
    fun getModelSends(): List<ModelSend> {
        return modelSends
    }

    override fun toString(): String {
        return "ServerState{" +
                "modelSends=" + modelSends +
                '}'
    }

    init {
        this.modelSends = ArrayList()
        for (modelSend in modelSends) {
            this.modelSends.add(ModelSend(modelSend.id, modelSend.matrix4))
        }
    }
}