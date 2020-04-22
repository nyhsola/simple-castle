package com.simple.castle.scene.game.object;

import com.simple.castle.object.GameObjectConstructor;
import com.simple.castle.object.absunit.AbstractGameObject;
import com.simple.castle.object.absunit.KinematicGameObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameSceneObjects {

    private final GameModelsConstructor gameModelsConstructor;
    private final Map<String, AbstractGameObject> sceneGameObjects;

    public GameSceneObjects(GameModelsConstructor gameModelsConstructor) {
        this.gameModelsConstructor = gameModelsConstructor;
        this.sceneGameObjects = new HashMap<>();
        initializeBasicMap();
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

    public void initializeBasicMap() {
        for (int i = 0; i < 800; i++) {
            String coneName = String.format("Cone.%03d", i);
            GameObjectConstructor constructor = gameModelsConstructor.getConstructor(coneName);
            if (constructor != null) {
                sceneGameObjects.put(coneName, new KinematicGameObject(constructor));
            }
        }
        sceneGameObjects.putAll(Map.ofEntries(
                Map.entry("Surface", new KinematicGameObject(gameModelsConstructor.getConstructor("Surface"))),
                Map.entry("Castle-1", new KinematicGameObject(gameModelsConstructor.getConstructor("Castle-1"))),
                Map.entry("Castle-2", new KinematicGameObject(gameModelsConstructor.getConstructor("Castle-2"))),
                Map.entry("Castle-3", new KinematicGameObject(gameModelsConstructor.getConstructor("Castle-3"))),
                Map.entry("Castle-4", new KinematicGameObject(gameModelsConstructor.getConstructor("Castle-4"))),

                Map.entry("Area-Left-Down", new KinematicGameObject(gameModelsConstructor.getConstructor("Area-Left-Down"))),
                Map.entry("Area-Left-Up", new KinematicGameObject(gameModelsConstructor.getConstructor("Area-Left-Up"))),
                Map.entry("Area-Right-Down", new KinematicGameObject(gameModelsConstructor.getConstructor("Area-Right-Down"))),
                Map.entry("Area-Right-Up", new KinematicGameObject(gameModelsConstructor.getConstructor("Area-Right-Up"))),

                Map.entry("Spawner-Red-Left", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Red-Left"))),
                Map.entry("Spawner-Red-Right", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Red-Right"))),
                Map.entry("Spawner-Red-Up", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Red-Up"))),

                Map.entry("Spawner-Blue-Down", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Blue-Down"))),
                Map.entry("Spawner-Blue-Left", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Blue-Left"))),
                Map.entry("Spawner-Blue-Up", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Blue-Up"))),

                Map.entry("Spawner-Green-Down", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Green-Down"))),
                Map.entry("Spawner-Green-Left", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Green-Left"))),
                Map.entry("Spawner-Green-Right", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Green-Right"))),

                Map.entry("Spawner-Teal-Down", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Teal-Down"))),
                Map.entry("Spawner-Teal-Left", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Teal-Left"))),
                Map.entry("Spawner-Teal-Up", new KinematicGameObject(gameModelsConstructor.getConstructor("Spawner-Teal-Up")))
        ));
    }
}
