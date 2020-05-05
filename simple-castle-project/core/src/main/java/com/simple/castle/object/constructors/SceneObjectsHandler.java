package com.simple.castle.object.constructors;

import com.simple.castle.object.constructors.tool.Interact;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.utils.StringTool;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneObjectsHandler {

    private final Map<String, AbstractGameObject> sceneGameObjects;

    @SuppressWarnings("unchecked")
    private SceneObjectsHandler(ObjectConstructors objectConstructors, List<Map<String, Object>> objects) {
        this.sceneGameObjects = new HashMap<>();
        objects.stream()
                .map(map -> new Object[]{map.get("interact"), StringTool.getValuesByPattern(objectConstructors.getAllConstructors(), (String) map.get("model"))})
                .forEach(object ->
                {
                    Collection<String> strings = (Collection<String>) object[1];
                    strings.forEach(s ->
                    {
                        Interact interactType = Interact.valueOf((String) object[0]);
                        AbstractGameObject gameObject = interactType.build(objectConstructors.getConstructor(s));
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

    public boolean contains(String sceneObj) {
        return sceneGameObjects.containsKey(sceneObj);
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

        public SceneObjectsHandler build(List<Map<String, Object>> objects) {
            return new SceneObjectsHandler(objectConstructors, objects);
        }
    }
}
