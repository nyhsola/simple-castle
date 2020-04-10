package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.utils.GameObjectsUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GameModels {
    public final static short GROUND_FLAG = 1 << 8;
    public final static short OBJECT_FLAG = 1 << 9;

    private final static short ALL_FLAG = -1;

    private final Model mainModel;
    private final ArrayMap<String, GameObjectConstructor> constructors;

    private final Map<List<Object>, Function<BoundingBox, btCollisionShape>> nodeShape = Map.ofEntries(
            Map.entry(Arrays.asList(0f, "Surface"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(1f, "Unit-1"), GameObjectsUtil::calculateSphere),
            Map.entry(Arrays.asList(0f, "Castle-1"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Castle-2"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Castle-3"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Castle-4"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-1"), GameObjectsUtil::calculateBox));

    public GameModels(Model mainModel) {
        this.mainModel = mainModel;

        constructors = new ArrayMap<>();
        constructors.putAll(constructObjects());
    }

    public Map<String, GameObject> constructNextModels(Map<String, GameObjectType> toConstruct) {
        Map<String, GameObject> constructed = new HashMap<>();
        toConstruct.forEach((nodeName, gameObjectType) ->
                constructed.put(nodeName,
                        gameObjectType == GameObjectType.OBJECT
                                ? constructObject(nodeName)
                                : constructStaticObject(nodeName)));
        return constructed;
    }

    private ArrayMap<String, GameObjectConstructor> constructObjects() {
        final BoundingBox tmp = new BoundingBox();
        ArrayMap<String, GameObjectConstructor> constructors = new ArrayMap<>(String.class, GameObjectConstructor.class);
        nodeShape.forEach((array, boundingBoxbtCollisionShapeFunction) -> {
                    float mass = (float) array.get(0);
                    String node = (String) array.get(1);
                    btCollisionShape shape = boundingBoxbtCollisionShapeFunction.apply(mainModel.getNode(node).calculateBoundingBox(tmp));
                    constructors.put(node, new GameObjectConstructor(mainModel, node, shape, mass));
                }
        );
        return constructors;
    }

    private GameObject constructStaticObject(String value) {
        GameObject object = constructors.get(value).construct();
        object.body.setCollisionFlags(object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        object.body.setContactCallbackFlag(GROUND_FLAG);
        object.body.setContactCallbackFilter(0);
        object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        return object;
    }

    private GameObject constructObject(String value) {
        GameObject obj = constructors.get(value).construct();
        obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.body.setContactCallbackFlag(OBJECT_FLAG);
        obj.body.setContactCallbackFilter(GROUND_FLAG);
        return obj;
    }

    public void dispose() {
        for (GameObjectConstructor constructor : constructors.values()) {
            constructor.dispose();
        }
        constructors.clear();
    }

    public Model getMainModel() {
        return mainModel;
    }
}
