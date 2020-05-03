package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.listener.RemoveListener;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.constructors.SceneObjectsHandler;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.object.unit.absunit.AbstractGameObject;

import java.util.*;
import java.util.stream.Stream;

public class GameUnitController implements CollisionEvent {

    private static final Vector3 tempVector = new Vector3();

    private static final List<String> redLeftPath = Arrays.asList(
            "area-1-1", "area-1-1-1", "area-2-3", "area-2",
            "area-2-1", "area-2-1-1", "area-3-3", "area-3");
    private static final Set<String> redEnemy = new HashSet<>(Arrays.asList(
            "castle-2", "castle-3", "castle-4"));

    private final Map<String, UnitGameObject> unitGameObjects = new HashMap<>();
    private final SceneObjectsHandler sceneObjectsHandler;
    private final Vector3 redLeftSpawnPosition;
    private final RemoveListener removeListener;

    private long previousTimeForUpdateTarget = System.currentTimeMillis();

    public GameUnitController(SceneObjectsHandler sceneObjectsHandler, RemoveListener removeListener) {
        this.sceneObjectsHandler = sceneObjectsHandler;
        this.redLeftSpawnPosition = sceneObjectsHandler.getSceneObject("area-1-1").transform.getTranslation(tempVector).cpy();
        this.removeListener = removeListener;
    }

    public void update() {
        if (System.currentTimeMillis() - previousTimeForUpdateTarget > 200) {
            unitGameObjects.forEach((key, value) -> value.updateTarget());
            previousTimeForUpdateTarget = System.currentTimeMillis();
        }
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Object userDataObj1 = object1.userData;
        Object userDataObj2 = object2.userData;
        if (userDataObj1 instanceof String && userDataObj2 instanceof String) {
            String userData1 = (String) userDataObj1;
            String userData2 = (String) userDataObj2;

            castleCollision(userData1, userData2);
            moveByPath(userData1, userData2);
        }
    }

    private void castleCollision(String userData1, String userData2) {
        UnitGameObject unitGameObject = Stream.of(userData1, userData2)
                .map(unitGameObjects::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        AbstractGameObject kinematicObject = Stream.of(userData1, userData2)
                .filter(redEnemy::contains)
                .map(sceneObjectsHandler::getSceneObject)
                .findFirst()
                .orElse(null);

        if (unitGameObject != null && kinematicObject != null) {
            removeListener.remove(kinematicObject);
        }
    }

    private void moveByPath(String userData1, String userData2) {
        UnitGameObject unitGameObject = Stream.of(userData1, userData2)
                .map(unitGameObjects::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        AbstractGameObject kinematicObject = Stream.of(userData1, userData2)
                .filter(redLeftPath::contains)
                .map(sceneObjectsHandler::getSceneObject)
                .findFirst()
                .orElse(null);

        if (unitGameObject != null && kinematicObject != null) {
            String nextAvailable = getNextAvailable(redLeftPath, kinematicObject.name);
            AbstractGameObject sceneObject = sceneObjectsHandler.getSceneObject(nextAvailable);
            unitGameObject.setTarget(sceneObject.transform.getTranslation(tempVector).cpy());
        }
    }

    private String getNextAvailable(List<String> list, String current) {
        int i = list.indexOf(current);
        if (i >= 0 && i < list.size() - 1) {
            i += 1;
        }
        return list.get(i);
    }

    public UnitGameObject spawnUnit(ObjectConstructors objectConstructors) {
        String uuid = UUID.randomUUID().toString();
        UnitGameObject unitGameObject = new UnitGameObject(objectConstructors.getConstructor("unit-type-1"));
        unitGameObject.body.setWorldTransform(new Matrix4());
        unitGameObject.body.translate(redLeftSpawnPosition);
        unitGameObject.body.userData = uuid;
        unitGameObjects.put(uuid, unitGameObject);
        return unitGameObject;
    }
}