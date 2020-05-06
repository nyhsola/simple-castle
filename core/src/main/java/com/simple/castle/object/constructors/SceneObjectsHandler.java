package com.simple.castle.object.constructors;

import com.simple.castle.object.constructors.tool.Interact;
import com.simple.castle.object.unit.abs.AbstractGameObject;
import com.simple.castle.utils.StringTool;
import com.simple.castle.utils.jsondto.SceneObjectJson;

import java.util.*;
import java.util.stream.Collectors;

public class SceneObjectsHandler {

    private final Map<String, AbstractGameObject> sceneGameObjects = new HashMap<>();
    private final List<AbstractGameObject> drawables = new ArrayList<>();

    private SceneObjectsHandler(ObjectConstructors objectConstructors, List<SceneObjectJson> objects) {
        objects.forEach(object -> {
            Collection<String> constructors = StringTool.getValuesByPattern(objectConstructors.getAllConstructors(), object.getModel());
            constructors.forEach(s -> {
                Interact interactType = Interact.valueOf(object.getInteract());
                AbstractGameObject gameObject = interactType.build(objectConstructors.getConstructor(s));
                gameObject.hide = Boolean.parseBoolean(object.getHide());
                this.addSceneObject(gameObject);
            });
        });
        this.updateDrawables();
    }

    public void dispose() {
        sceneGameObjects.forEach((s, gameObject) -> gameObject.dispose());
    }

    public void addSceneObject(AbstractGameObject gameObject) {
        sceneGameObjects.put(String.valueOf(gameObject.body.userData), gameObject);
        this.updateDrawables();
    }

    public void addSceneObjects(List<? extends AbstractGameObject> gameObjects) {
        gameObjects.forEach(abstractGameObject ->
                sceneGameObjects.put(String.valueOf(abstractGameObject.body.userData), abstractGameObject));
        this.updateDrawables();
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

    public Collection<AbstractGameObject> getDrawObjects() {
        return drawables;
    }

    public void disposeObject(AbstractGameObject object) {
        sceneGameObjects.remove(String.valueOf(object.body.userData));
        object.dispose();
        this.updateDrawables();
    }

    private void updateDrawables() {
        drawables.clear();

        List<AbstractGameObject> drawabledCollected = sceneGameObjects.values().stream()
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
