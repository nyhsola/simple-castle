package com.simple.castle.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class ServerState implements Serializable {
    private final List<ModelSend> modelSends;

    public ServerState(List<ModelSend> modelSends) {
        this.modelSends = new ArrayList<>();

        for (ModelSend modelSend : modelSends) {
            this.modelSends.add(new ModelSend(modelSend.getId(), modelSend.getMatrix4()));
        }
    }

    public List<ModelSend> getModelSends() {
        return modelSends;
    }

    @Override
    public String toString() {
        return "ServerState{" +
                "modelSends=" + modelSends +
                '}';
    }
}
