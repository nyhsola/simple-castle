package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.constructors.SceneObjectsConstructor;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.object.unit.absunit.AbstractGameObject;

import java.util.*;
import java.util.stream.Stream;

public class GameUnitController implements CollisionEvent {

    private static final Vector3 tempVector = new Vector3();
    private static final List<String> redLeftPath = Arrays.asList("area-1-1", "area-1-1-1", "area-2-3");

    private final SceneObjectsConstructor sceneObjectsConstructor;
    private final Vector3 redLeftSpawnPosition;
    private final Map<String, UnitGameObject> unitGameObjects = new HashMap<>();

    private long previousTimeForUpdateTarget = System.currentTimeMillis();

    public GameUnitController(SceneObjectsConstructor sceneObjectsConstructor) {
        this.sceneObjectsConstructor = sceneObjectsConstructor;
        this.redLeftSpawnPosition = sceneObjectsConstructor.getSceneObject("area-1-1").transform.getTranslation(tempVector).cpy();
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

            UnitGameObject unitGameObject = Stream.of(userData1, userData2)
                    .map(unitGameObjects::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            AbstractGameObject kinematicObject = Stream.of(userData1, userData2)
                    .filter(redLeftPath::contains)
                    .map(sceneObjectsConstructor::getSceneObject)
                    .findFirst()
                    .orElse(null);

            if (unitGameObject != null && kinematicObject != null) {
                if (kinematicObject.name.equals("area-1-1")) {
                    AbstractGameObject sceneObject = sceneObjectsConstructor.getSceneObject("area-1-1-1");
                    unitGameObject.setTarget(sceneObject.transform.getTranslation(tempVector).cpy());
                }

                if (kinematicObject.name.equals("area-1-1-1")) {
                    AbstractGameObject sceneObject = sceneObjectsConstructor.getSceneObject("area-2-3");
                    unitGameObject.setTarget(sceneObject.transform.getTranslation(tempVector).cpy());
                }
                Gdx.app.log("", unitGameObject.name + " " + unitGameObject.body.userData + " " + kinematicObject.name);
            }
        }
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