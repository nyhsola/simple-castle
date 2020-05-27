package com.simple.castle.client.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.simple.castle.core.ModelSend;
import com.simple.castle.core.ServerState;

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

    public Collection<ModelInstance> updateAndGet(ServerState serverState) {
        if (serverState != null && serverState.getModelSends() != null) {
            for (ModelSend modelSend : serverState.getModelSends()) {
                if (!modelInstances.containsKey(modelSend.getId())) {
                    ModelInstance value = new ModelInstance(model, modelSend.getId(), true);
                    value.transform.set(modelSend.getMatrix4());
                    modelInstances.put(modelSend.getId(), value);
                }
            }
        }
        return modelInstances.values();
    }

}
