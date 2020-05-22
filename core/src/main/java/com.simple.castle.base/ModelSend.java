package com.simple.castle.base;

import com.badlogic.gdx.math.Matrix4;

import java.io.Serializable;

public class ModelSend implements Serializable {
    private String id;
    private Matrix4 matrix4;

    public ModelSend() {
    }

    public ModelSend(ModelSend modelSend) {
        this.id = modelSend.id;
        this.matrix4 = new Matrix4(modelSend.matrix4);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Matrix4 getMatrix4() {
        return matrix4;
    }

    public void setMatrix4(Matrix4 matrix4) {
        this.matrix4 = matrix4;
    }

    @Override
    public String toString() {
        return "ModelSend{" +
                "id='" + id + '\'' +
                ", matrix4=" + matrix4 +
                '}';
    }
}
