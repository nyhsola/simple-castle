package com.simple.castle.server.manager;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;
import com.simple.castle.base.ModelSend;
import com.simple.castle.server.composition.BaseObject;
import com.simple.castle.server.composition.Constructor;
import com.simple.castle.server.json.SceneObjectsJson;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SceneManager implements Disposable {
    private final Map<String, Constructor> constructorMap;
    private final Map<String, BaseObject> baseObjectMap;
    private final List<ModelInstance> drawables;
    private final List<ModelSend> modelsSend;

    public SceneManager(SceneObjectsJson sceneObjectsJson, Model model) {
        constructorMap = sceneObjectsJson.getSceneObjectsJson()
                .stream()
                .map(sceneObjectJson -> new Constructor(model,
                        sceneObjectJson.getNodePattern(),
                        sceneObjectJson.getInteract(),
                        sceneObjectJson.getShape(),
                        sceneObjectJson.getInstantiate(),
                        sceneObjectJson.getHide()))
                .collect(Collectors.toMap(Constructor::getId, Function.identity()));

        baseObjectMap = constructorMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getInstantiate())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new BaseObject(entry.getValue())));

        drawables = baseObjectMap.values().stream()
                .filter(baseObject -> !baseObject.getHide())
                .map(BaseObject::getModelInstance).collect(Collectors.toList());

        modelsSend = baseObjectMap.values().stream()
                .filter(baseObject -> !baseObject.getHide())
                .map(baseObject -> {
                    ModelSend modelSend = new ModelSend();
                    modelSend.setId(baseObject.getId());
                    modelSend.setMatrix4(baseObject.getModelInstance().transform);
                    return modelSend;
                }).collect(Collectors.toList());
    }

    public BaseObject getObject(String id) {
        return baseObjectMap.get(id);
    }

    public List<ModelInstance> getDrawables() {
        return drawables;
    }

    public Collection<BaseObject> getAll() {
        return baseObjectMap.values();
    }

    public List<ModelSend> getSend() {
        return modelsSend;
    }

    @Override
    public void dispose() {
        baseObjectMap.values().forEach(BaseObject::dispose);
    }
}
