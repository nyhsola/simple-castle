package com.simple.castle.scene.game.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.simple.castle.listener.CollisionEvent;
import com.simple.castle.listener.SceneObjectManager;
import com.simple.castle.object.constructors.ObjectConstructors;
import com.simple.castle.object.constructors.SceneObjectsHandler;
import com.simple.castle.object.unit.BasicUnit;
import com.simple.castle.object.unit.abs.AbstractGameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerController implements CollisionEvent {

    public static final long spawnEvery = 1 * 1000;
    private static final Vector3 tempVector1 = new Vector3();

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
    private static final List<String> blueRightPath = Arrays.asList(
            "area-2-3", "area-1-1-1", "area-1-1", "area-1",
            "area-1-3", "area-4-1-1", "area-4-1", "area-4"
    );
    private static final Vector3 tempVector2 = new Vector3();
    private final ObjectConstructors objectConstructors;
    private final SceneObjectManager sceneObjectManager;

    private final List<Player> players;
    private long timeLeft = spawnEvery;
    private long previousTime = System.currentTimeMillis();
    private final SceneObjectsHandler sceneObjectsHandler;

    public PlayerController(ObjectConstructors objectConstructors, SceneObjectsHandler sceneObjectsHandler,
                            SceneObjectManager sceneObjectManager) {
        this.objectConstructors = objectConstructors;
        this.sceneObjectsHandler = sceneObjectsHandler;
        this.sceneObjectManager = sceneObjectManager;

        // TODO: 5/5/2020 Move SceneObjectsHandler to GameScene, unit-types and paths load from file
        players = new ArrayList<>();
        players.add(new Player("unit-type-1",
                toAbsList(sceneObjectsHandler, Stream.of(redLeftPath, redMiddlePath, redRightPath))));
        players.add(new Player("unit-type-2",
                toAbsList(sceneObjectsHandler, Stream.of(blueRightPath))));
    }

    private static List<List<AbstractGameObject>> toAbsList(SceneObjectsHandler sceneObjectsHandler,
                                                            Stream<List<String>> paths) {
        return paths.map(pathsString -> pathsString.stream()
                .map(sceneObjectsHandler::getSceneObjectByModelName)
                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public void collisionEvent(btCollisionObject object1, btCollisionObject object2) {
        Object userDataObj1 = object1.userData;
        Object userDataObj2 = object2.userData;
        if (userDataObj1 instanceof String && userDataObj2 instanceof String) {
            String userData1 = (String) userDataObj1;
            String userData2 = (String) userDataObj2;
            if (sceneObjectsHandler.contains(userData1) && sceneObjectsHandler.contains(userData2)) {
                AbstractGameObject sceneObj1 = sceneObjectsHandler.getSceneObjectByUserData(userData1);
                AbstractGameObject sceneObj2 = sceneObjectsHandler.getSceneObjectByUserData(userData2);
                players.forEach(player -> {
                    if (player.isPlayers(sceneObj1)) {
                        player.collisionEvent((BasicUnit) sceneObj1, sceneObj2);
                    }
                    if (player.isPlayers(sceneObj2)) {
                        player.collisionEvent((BasicUnit) sceneObj2, sceneObj1);
                    }
                });
            }
        }
    }

    public void update() {
        long diff = System.currentTimeMillis() - previousTime;
        timeLeft = timeLeft - diff;

        if (timeLeft <= 0) {
            timeLeft = spawnEvery;
            List<BasicUnit> basicUnits = this.spawnUnits(objectConstructors);
            basicUnits.forEach(sceneObjectManager::add);
        }

        // TODO: 5/5/2020 Check distances between objects
        players.forEach(Player::update);

        List<BasicUnit> units = players.stream()
                .map(Player::getUnits)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        for (int i = 0; i < units.size(); i++) {
            for (int j = 0; j < units.size(); j++) {
                if (i != j) {
                    BasicUnit unit1 = units.get(i);
                    BasicUnit unit2 = units.get(j);

                    Vector3 unit1P = unit1.body.getWorldTransform().getTranslation(tempVector1);
                    Vector3 unit2P = unit2.body.getWorldTransform().getTranslation(tempVector2);

                    Player playerWhoseUnitsSameColor = players.stream()
                            .filter(player -> player.isPlayers(unit1) && player.isPlayers(unit2))
                            .findAny()
                            .orElse(null);
                    float dst = unit1P.dst(unit2P);
                    if (playerWhoseUnitsSameColor == null) {
                        unit1.unitNear(unit2, dst);
                        unit2.unitNear(unit1, dst);
                    }
                }
            }
        }

        previousTime = System.currentTimeMillis();
    }

    private List<BasicUnit> spawnUnits(ObjectConstructors objectConstructors) {
        return players.stream()
                .map(player -> player.spawnUnitsOnStartPositions(objectConstructors))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}