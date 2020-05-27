package com.simple.castle.core;

import com.badlogic.gdx.math.Matrix4;

import java.io.Serializable;

public final class ModelSend implements Serializable {
    private final String id;
    private final Matrix4 matrix4;

    public ModelSend(String id, Matrix4 matrix4) {
        this.id = id;
        this.matrix4 = new Matrix4(matrix4);
    }

    public String getId() {
        return id;
    }

    public Matrix4 getMatrix4() {
        return matrix4;
    }

    @Override
    public String toString() {
        return "ModelSend{" +
                "id='" + id + '\'' +
                ", matrix4=" + matrix4 +
                '}';
    }
}
