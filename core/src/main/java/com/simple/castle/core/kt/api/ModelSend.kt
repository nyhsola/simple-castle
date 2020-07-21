package com.simple.castle.core.kt.api

import com.badlogic.gdx.math.Matrix4
import java.io.Serializable

class ModelSend(val id: String?, matrix4: Matrix4?) : Serializable {
    val matrix4: Matrix4 = Matrix4(matrix4)

    override fun toString(): String {
        return "ModelSend{" +
                "id='" + id + '\'' +
                ", matrix4=" + matrix4 +
                '}'
    }

}