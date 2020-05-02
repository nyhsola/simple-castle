package com.simple.castle.object.constructors;

import com.simple.castle.object.constructors.tool.Interact;
import com.simple.castle.object.unit.absunit.AbstractGameObject;
import com.simple.castle.utils.ModelUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneObjectsConstructor {

    private final Map<String, AbstractGameObject> sceneGameObjects;

    @SuppressWarnings("unchecked")
    private SceneObjectsConstructor(ObjectConstructors objectConstructors, List<Map<String, Object>> objects) {
        this.sceneGameObjects = new HashMap<>();
        objects.stream()
                .map(map -> new Object[]{map.get("interact"), ModelUtils.getValuesByPattern(objectConstructors.getAllConstructors(), (String) map.get("model"))})
                .forEach(object ->
                {
                    Collection<String> strings = (Collection<String>) object[1];
                    strings.forEach(s ->
                            sceneGameObjects.put(s, Interact.valueOf((String) object[0]).build(objectConstructors.getConstructor(s))));
                });
    }

    public void dispose() {
        sceneGameObjects.forEach((s, gameObject) -> gameObject.dispose());
    }

    public void addSceneObject(String name, AbstractGameObject gameObject) {
        sceneGameObjects.put(name, gameObject);
    }

    public AbstractGameObject getSceneObject(String name) {
        return sceneGameObjects.get(name);
    }

    public Collection<AbstractGameObject> getSceneObjects() {
        return sceneGameObjects.values();
    }

    public static final class Builder {
        private final ObjectConstructors objectConstructors;

        public Builder(ObjectConstructors objectConstructors) {
            this.objectConstructors = objectConstructors;
        }

        public SceneObjectsConstructor build(List<Map<String, Object>> objects) {
            return new SceneObjectsConstructor(objectConstructors, objects);
        }
    }
}
