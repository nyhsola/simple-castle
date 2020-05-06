package com.simple.castle.object.constructors;

import com.simple.castle.object.constructors.tool.Interact;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.utils.StringTool;
import com.simple.castle.utils.jsondto.SceneObjectJson;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneObjectsHandler {

    private final Map<String, AbstractGameObject> sceneGameObjects;

    @SuppressWarnings("unchecked")
    private SceneObjectsHandler(ObjectConstructors objectConstructors, List<SceneObjectJson> objects) {
        this.sceneGameObjects = new HashMap<>();
        objects.forEach(object ->
        {
            Collection<String> constructors = StringTool.getValuesByPattern(objectConstructors.getAllConstructors(), object.getModel());
            constructors.forEach(s -> {
                Interact interactType = Interact.valueOf(object.getInteract());
                AbstractGameObject gameObject = interactType.build(objectConstructors.getConstructor(s));
                gameObject.hide = Boolean.parseBoolean(object.getHide());
                sceneGameObjects.put(String.valueOf(gameObject.body.userData), gameObject);
            });
        });
    }

    public void dispose() {
        sceneGameObjects.forEach((s, gameObject) -> gameObject.dispose());
    }

    public void addSceneObject(AbstractGameObject gameObject) {
        sceneGameObjects.put(String.valueOf(gameObject.body.userData), gameObject);
    }

    public boolean contains(AbstractGameObject abstractGameObject) {
        return contains(String.valueOf(abstractGameObject.body.userData));
    }

    public boolean contains(String userData) {
        return sceneGameObjects.containsKey(userData);
    }

    public AbstractGameObject getSceneObjectByModelName(String name) {
        return sceneGameObjects.values()
                .stream()
                .filter(abstractGameObject -> abstractGameObject.nodes.size >= 1 && abstractGameObject.nodes.get(0).id.equals(name))
                .findFirst()
                .orElse(null);
    }

    public AbstractGameObject getSceneObjectByUserData(String name) {
        return sceneGameObjects.get(name);
    }

    public Collection<AbstractGameObject> getSceneObjects() {
        return sceneGameObjects.values();
    }

    public void disposeObject(AbstractGameObject object) {
        sceneGameObjects.remove(String.valueOf(object.body.userData));
        object.dispose();
    }

    public static final class Builder {
        private final ObjectConstructors objectConstructors;

        public Builder(ObjectConstructors objectConstructors) {
            this.objectConstructors = objectConstructors;
        }

        public SceneObjectsHandler build(List<SceneObjectJson> objects) {
            return new SceneObjectsHandler(objectConstructors, objects);
        }
    }
}
