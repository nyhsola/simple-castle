package com.simple.castle.client.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.simple.castle.base.ModelSend;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSceneManager {
    private final Model model;
    private final Map<String, ModelInstance> modelInstances;

    public ClientSceneManager(Model model) {
        this.model = model;
        this.modelInstances = new HashMap<>();
    }

    public Collection<ModelInstance> updateAndGet(List<ModelSend> modelSends) {
        if (modelSends != null) {
            for (ModelSend modelSend : modelSends) {
                if (!modelInstances.containsKey(modelSend.getId())) {
                    modelInstances.put(modelSend.getId(), new ModelInstance(model, modelSend.getId(), true));
                }
            }
        }
        return modelInstances.values();
    }

}
