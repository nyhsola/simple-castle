package com.simple.castle.client.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.simple.castle.base.ModelSend;
import com.simple.castle.base.ServerRespond;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientSceneManager {
    private final Model model;
    private final Map<String, ModelInstance> modelInstances;

    public ClientSceneManager(Model model) {
        this.model = model;
        this.modelInstances = new HashMap<>();
    }

    public Collection<ModelInstance> updateAndGet(ServerRespond serverRespond) {
        if (serverRespond != null && serverRespond.getModelSends() != null) {
            for (ModelSend modelSend : serverRespond.getModelSends()) {
                if (!modelInstances.containsKey(modelSend.getId())) {
                    modelInstances.put(modelSend.getId(), new ModelInstance(model, modelSend.getId(), true));
                }
            }
        }
        return modelInstances.values();
    }

}
