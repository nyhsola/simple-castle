package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.google.common.collect.ImmutableSet;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.object.absunit.AbstractGameObject;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.scene.game.object.GameModelsConstructor;
import com.simple.castle.scene.game.object.GameSceneObjects;

import java.util.*;
import java.util.stream.Stream;

public class GameUnitController implements CollisionEvent {

    private static final Vector3 tempVector = new Vector3();
    private static final Set<String> redLeftPath = ImmutableSet.of("Spawner-Red-Left", "Area-Left-Down", "Spawner-Blue-Down");

    private final GameSceneObjects gameSceneObjects;
    private final Vector3 redLeftSpawnPosition;
    private final Map<String, UnitGameObject> unitGameObjects = new HashMap<>();

    private long previousTime = System.currentTimeMillis();

    public GameUnitController(GameSceneObjects gameSceneObjects) {
        this.gameSceneObjects = gameSceneObjects;
        this.redLeftSpawnPosition = gameSceneObjects.getSceneObject("Spawner-Red-Left").transform.getTranslation(tempVector).cpy();
    }

    public void update() {
        if (System.currentTimeMillis() - previousTime > 1000) {
            unitGameObjects.forEach((key, value) -> updateLinearVelocity(value));
            previousTime = System.currentTimeMillis();
        }
    }

    private void updateLinearVelocity(UnitGameObject unit) {
        AbstractGameObject movingTo = unit.getMovingTo();
        if (movingTo != null) {
            Vector3 unitV = unit.transform.getTranslation(tempVector).cpy();
            Vector3 toObjectV = movingTo.transform.getTranslation(tempVector).cpy();
            unit.body.setLinearVelocity(toObjectV.sub(unitV).nor().scl(5));
        }
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Object userDataObj1 = object1.userData;
        Object userDataObj2 = object2.userData;
        if (userDataObj1 instanceof String && userDataObj2 instanceof String) {
            String userData1 = (String) userDataObj1;
            String userData2 = (String) userDataObj2;

            UnitGameObject unitGameObject = Stream.of(userData1, userData2)
                    .map(unitGameObjects::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            AbstractGameObject kinematicObject = Stream.of(userData1, userData2)
                    .filter(redLeftPath::contains)
                    .map(gameSceneObjects::getSceneObject)
                    .findFirst()
                    .orElse(null);

            if (unitGameObject != null && kinematicObject != null) {
                if (kinematicObject.name.equals("Spawner-Red-Left")) {
                    unitGameObject.setMovingTo(gameSceneObjects.getSceneObject("Area-Left-Down"));
                }

                if (kinematicObject.name.equals("Area-Left-Down")) {
                    unitGameObject.setMovingTo(gameSceneObjects.getSceneObject("Spawner-Blue-Down"));
                }
                Gdx.app.log("", unitGameObject.name + " " + unitGameObject.body.userData + " " + kinematicObject.name);
            }
        }
    }

    private String getNextDirection(String currentDirection) {
        if (currentDirection.equals("Spawner-Red-Left")) {
            return "Area-Left-Down";
        }
        if (currentDirection.equals("Area-Left-Down")) {
            return "Spawner-Blue-Down";
        }
        return "";
    }

    public UnitGameObject spawnUnit(GameModelsConstructor gameModelsConstructor) {
        String uuid = UUID.randomUUID().toString();
        UnitGameObject unitGameObject = new UnitGameObject(gameModelsConstructor.getConstructor("Unit-1"));
        unitGameObject.body.setWorldTransform(new Matrix4());
        unitGameObject.body.translate(redLeftSpawnPosition);
        unitGameObject.body.userData = uuid;
        unitGameObjects.put(uuid, unitGameObject);
        return unitGameObject;
    }
}