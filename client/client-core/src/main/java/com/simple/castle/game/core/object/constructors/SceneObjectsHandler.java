package com.simple.castle.game.core.object.constructors;

import com.simple.castle.server.game.core.object.constructors.tool.Interact;
import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;
import com.simple.castle.server.game.core.settings.dto.SceneObjectsJson;
import com.simple.castle.server.game.core.settings.dto.base.SceneObjectJson;
import com.simple.castle.server.game.core.utils.StringTool;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SceneObjectsHandler {

    private final Set<AbstractGameObject> sceneGameObjects = new HashSet<>();
    private final Set<AbstractGameObject> drawables = new HashSet<>();

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

        public SceneObjectsHandler build(SceneObjectsJson objects) {
            return new SceneObjectsHandler(objectConstructors, objects.getSceneObjects());
        }
    }
}
