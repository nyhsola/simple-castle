package com.simple.castle.object.constructors;

import com.simple.castle.object.constructors.tool.Interact;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.utils.StringTool;
import com.simple.castle.utils.jsondto.SceneObjectJson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SceneObjectsHandler {

    private final List<AbstractGameObject> sceneGameObjects = new ArrayList<>();
    private final List<AbstractGameObject> drawables = new ArrayList<>();

    private SceneObjectsHandler(ObjectConstructors objectConstructors, List<SceneObjectJson> objects) {
        objects.forEach(object -> {
            Collection<String> constructors = StringTool.getValuesByPattern(objectConstructors.getAllConstructors(), object.getModel());
            constructors.forEach(s -> {
                Interact interactType = Interact.valueOf(object.getInteract());
                AbstractGameObject gameObject = interactType.build(objectConstructors.getConstructor(s));
                gameObject.hide = Boolean.parseBoolean(object.getHide());
                this.add(gameObject);
            });
        });
        this.updateDrawables();
    }

    public void dispose() {
        sceneGameObjects.forEach(AbstractGameObject::dispose);
        sceneGameObjects.clear();
    }

    public void add(AbstractGameObject gameObject) {
        sceneGameObjects.add(gameObject);
        this.updateDrawables();
    }

    public void addAll(List<? extends AbstractGameObject> gameObjects) {
        sceneGameObjects.addAll(gameObjects);
        this.updateDrawables();
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
        this.updateDrawables();
    }

    private void updateDrawables() {
        drawables.clear();
        List<AbstractGameObject> drawabledCollected = sceneGameObjects.stream()
                .filter(abstractGameObject -> !abstractGameObject.hide)
                .collect(Collectors.toList());
        drawables.addAll(drawabledCollected);
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
