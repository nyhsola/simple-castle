package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.google.common.collect.ImmutableSet;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.object.unit.UnitGameObject;
import com.simple.castle.scene.game.object.GameModelsConstructor;
import com.simple.castle.scene.game.object.GameSceneObjects;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class GameUnitController implements CollisionEvent {

    private static final Vector3 tempVector = new Vector3();
    private static final Set<String> redLeftPath = ImmutableSet.of("Spawner-Red-Left", "Area-Left-Down", "Spawner-Blue-Down");

    private final GameSceneObjects gameSceneObjects;
    private final Vector3 redLeftSpawnPosition;
    private final Map<String, UnitGameObject> unitGameObjects = new HashMap<>();

    public GameUnitController(GameSceneObjects gameSceneObjects) {
        this.gameSceneObjects = gameSceneObjects;
        this.redLeftSpawnPosition = gameSceneObjects.getSceneObject("Spawner-Red-Left").transform.getTranslation(tempVector).cpy();
    }

    public void update() {
    }

    @Override
    public Predicate<Pair<btCollisionObject, btCollisionObject>> getEventFilter() {
        return btCollisionObjectPair -> {
            Object userDataObj1 = btCollisionObjectPair.getKey().userData;
            Object userDataObj2 = btCollisionObjectPair.getValue().userData;
            if (userDataObj1 instanceof String && userDataObj2 instanceof String) {
                String userData1 = (String) userDataObj1;
                String userData2 = (String) userDataObj2;
                return unitGameObjects.containsKey(userData1) || unitGameObjects.containsKey(userData2);
            }
            return false;
        };
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Gdx.app.log("", object1.userData + " " + object2.userData);
    }

    public UnitGameObject spawnUnit(GameModelsConstructor gameModelsConstructor) {
//        Vector3 areaLeftDown = gameSceneObjects.getSceneObject("Area-Left-Down").transform
//                .getTranslation(tempVector).cpy();
//        unitGameObject.body.setLinearVelocity(areaLeftDown.sub(spawnerRedLeft).scl(0.1f));
        String uuid = UUID.randomUUID().toString();
        UnitGameObject unitGameObject = new UnitGameObject(gameModelsConstructor.getConstructor("Unit-1"));
        unitGameObject.body.setWorldTransform(new Matrix4());
        unitGameObject.body.translate(redLeftSpawnPosition);
        unitGameObject.body.userData = uuid;
        unitGameObjects.put(uuid, unitGameObject);
        return unitGameObject;
    }
}