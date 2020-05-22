package com.simple.castle.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerRespond implements Serializable {
    private List<ModelSend> modelSends;

    public ServerRespond() {

    }

    public ServerRespond(ServerRespond serverRespond) {
        this.modelSends = new ArrayList<>();
        for (ModelSend modelSend : serverRespond.modelSends) {
            modelSends.add(new ModelSend(modelSend));
        }
    }

    public List<ModelSend> getModelSends() {
        return modelSends;
    }

    public void setModelSends(List<ModelSend> modelSends) {
        this.modelSends = modelSends;
    }
}
