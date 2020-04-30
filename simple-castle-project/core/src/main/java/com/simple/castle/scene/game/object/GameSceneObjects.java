package com.simple.castle.scene.game.object;

import com.simple.castle.object.absunit.AbstractGameObject;
import com.simple.castle.object.absunit.KinematicGameObject;
import com.simple.castle.scene.game.GameScene;
import com.simple.castle.utils.ModelUtils;
import com.simple.castle.utils.PropertyLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameSceneObjects {

    private final Map<String, AbstractGameObject> sceneGameObjects;

    public GameSceneObjects(GameModelsConstructor gameModelsConstructor) {
        this.sceneGameObjects = new HashMap<>();

        PropertyLoader.loadObjectsFromScene(GameScene.SCENE_NAME)
                .stream()
                .map(pattern -> ModelUtils.getValuesByPattern(gameModelsConstructor.getAllConstructors(), pattern))
                .flatMap(Collection::stream)
                .forEach(object ->
                        sceneGameObjects.put(object, new KinematicGameObject(gameModelsConstructor.getConstructor(object))));
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
}
