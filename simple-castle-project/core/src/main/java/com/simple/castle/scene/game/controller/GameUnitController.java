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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameUnitController implements CollisionEvent {

    private static final Vector3 tempVector = new Vector3();

    private static final List<String> redLeftPath = Arrays.asList(
            "area-1-1", "area-1-1-1", "area-2-3", "area-2",
            "area-2-1", "area-2-1-1", "area-3-3", "area-3");
    private static final List<String> redMiddlePath = Arrays.asList(
            "area-1-2", "area-0", "area-3-2", "area-3"
    );
    private static final List<String> redRightPath = Arrays.asList(
            "area-1-3", "area-4-1-1", "area-4-1", "area-4",
            "area-4-3", "area-3-1-1", "area-3-1", "area-3"
    );
    private static final Set<String> redEnemy = new HashSet<>(Arrays.asList(
            "castle-2", "castle-3", "castle-4"));

    private final Vector3 redLeftSpawnPosition;
    private final Vector3 redMiddleSpawnPosition;
    private final Vector3 redRightSpawnPosition;

    private final Map<String, UnitGameObject> unitGameObjects = new HashMap<>();
    private final SceneObjectsHandler sceneObjectsHandler;
    private final RemoveListener removeListener;

    private long previousTimeForUpdateTarget = System.currentTimeMillis();

    public GameUnitController(SceneObjectsHandler sceneObjectsHandler, RemoveListener removeListener) {
        this.sceneObjectsHandler = sceneObjectsHandler;
        this.redLeftSpawnPosition = sceneObjectsHandler.getSceneObject("area-1-1").transform.getTranslation(tempVector).cpy();
        this.redMiddleSpawnPosition = sceneObjectsHandler.getSceneObject("area-1-2").transform.getTranslation(tempVector).cpy();
        this.redRightSpawnPosition = sceneObjectsHandler.getSceneObject("area-1-3").transform.getTranslation(tempVector).cpy();
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

            moveByPath(userData1, userData2);
            castleCollision(userData1, userData2);
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
                .map(sceneObjectsHandler::getSceneObject)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (unitGameObject != null && kinematicObject != null) {
            List<String> path = Stream.of(redLeftPath, redMiddlePath, redRightPath)
                    .filter(strings -> strings.contains(kinematicObject.name))
                    .findAny()
                    .orElse(null);
            if (path != null) {
                String nextAvailable = getNextAvailable(path, kinematicObject.name);
                AbstractGameObject sceneObject = sceneObjectsHandler.getSceneObject(nextAvailable);
                unitGameObject.setTarget(sceneObject.transform.getTranslation(tempVector).cpy());
            }
        }
    }

    private String getNextAvailable(List<String> list, String current) {
        int i = list.indexOf(current);
        if (i >= 0 && i < list.size() - 1) {
            i += 1;
        }
        return list.get(i);
    }

    public List<UnitGameObject> spawnUnits(ObjectConstructors objectConstructors) {
        return spawnUnitsOnPositions(objectConstructors,
                redLeftSpawnPosition, redMiddleSpawnPosition, redRightSpawnPosition);
    }

    private List<UnitGameObject> spawnUnitsOnPositions(ObjectConstructors objectConstructors, Vector3... positions) {
        return Stream.of(positions)
                .map(vector3 -> {
                    UnitGameObject unit = new UnitGameObject(objectConstructors.getConstructor("unit-type-1"));
                    unit.body.setWorldTransform(new Matrix4());
                    unit.body.translate(vector3);
                    unit.body.userData = UUID.randomUUID().toString();
                    return unit;
                })
                .peek(unit -> {
                    unitGameObjects.put((String) unit.body.userData, unit);
                })
                .collect(Collectors.toList());
    }
}