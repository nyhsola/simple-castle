package com.simple.castle.core.object.constructors;

import com.simple.castle.core.object.constructors.tool.Interact;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.core.utils.StringTool;
import com.simple.castle.core.utils.jsondto.SceneObjectJson;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SceneObjectsHandler {

    private final Set<AbstractGameObject> sceneGameObjects = new HashSet<>();
    private final Set<AbstractGameObject> drawables = new HashSet<>();

    private SceneObjectsHandler(ObjectConstructors objectConstructors, List<com.simple.castle.core.utils.jsondto.SceneObjectJson> objects) {
        objects.forEach(object -> {
            Collection<String> constructors = StringTool.getValuesByPattern(objectConstructors.getAllConstructors(), object.getModel());
            constructors.forEach(s -> {
                Interact interactType = Interact.valueOf(object.getInteract());
                AbstractGameObject gameObject = interactType.build(objectConstructors.getConstructor(s));
                gameObject.hide = Boolean.parseBoolean(object.getHide());
                this.add(gameObject);
            });
        });
        sceneGameObjects.stream().filter(abstractGameObject -> !abstractGameObject.hide).forEach(drawables::add);
    }

    public void dispose() {
        sceneGameObjects.forEach(AbstractGameObject::dispose);
        sceneGameObjects.clear();
    }

    public void add(AbstractGameObject gameObject) {
        sceneGameObjects.add(gameObject);
        if (!gameObject.hide) {
            drawables.add(gameObject);
        }
    }

    public void addAll(List<? extends AbstractGameObject> gameObjects) {
        sceneGameObjects.addAll(gameObjects);
        gameObjects.stream().filter(abstractGameObject -> !abstractGameObject.hide).forEach(drawables::add);
    }

    public boolean contains(AbstractGameObject abstractGameObject) {
        return sceneGameObjects.contains(abstractGameObject);
    }

    public AbstractGameObject getByName(String name) {
        return sceneGameObjects.stream()
                .filter(abstractGameObject ->
                        abstractGameObject.nodes.size >= 1 && abstractGameObject.nodes.get(0).id.equals(name))
                .findFirst()
                .orElse(null);
    }

    public Collection<AbstractGameObject> getSceneObjects() {
        return sceneGameObjects;
    }

    public Collection<AbstractGameObject> getDrawObjects() {
        return drawables;
    }

    public void remove(AbstractGameObject object) {
        sceneGameObjects.remove(object);
        object.dispose();
        drawables.remove(object);
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