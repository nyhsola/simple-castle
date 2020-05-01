package com.simple.castle.object.constructors;

import com.simple.castle.object.unit.absunit.AbstractGameObject;
import com.simple.castle.object.unit.absunit.KinematicGameObject;
import com.simple.castle.utils.ModelUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneObjectsConstructor {

    private final Map<String, AbstractGameObject> sceneGameObjects;

    private SceneObjectsConstructor(ObjectConstructors objectConstructors, List<String> objects) {
        this.sceneGameObjects = new HashMap<>();

        objects.stream()
                .map(pattern -> ModelUtils.getValuesByPattern(objectConstructors.getAllConstructors(), pattern))
                .flatMap(Collection::stream)
                .forEach(object ->
                        sceneGameObjects.put(object, new KinematicGameObject(objectConstructors.getConstructor(object))));
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

        public SceneObjectsConstructor build(List<String> objects) {
            return new SceneObjectsConstructor(objectConstructors, objects);
        }
    }
}
