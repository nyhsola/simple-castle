package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.object.GameObject.Constructor;
import com.simple.castle.launcher.main.utils.GameObjectsUtil;
import com.simple.castle.launcher.main.utils.ModelLoader;

import java.util.Arrays;
import java.util.List;

public class ModelFactory extends ApplicationAdapter {
    private final static short GROUND_FLAG = 1 << 8;
    private final static short OBJECT_FLAG = 1 << 9;
    private final static short ALL_FLAG = -1;

    private final ArrayMap<String, GameObject.Constructor> constructorsArrayMap = new ArrayMap<>();
    private Model mainModel;

    @Override
    public void create() {
        mainModel = ModelLoader.loadModel();
        constructorsArrayMap.putAll(constructObjects());
    }

    private ArrayMap<String, GameObject.Constructor> constructObjects() {
        ArrayMap<String, Constructor> constructors = new ArrayMap<>(String.class, Constructor.class);
        constructors.put("Surface", new Constructor(mainModel, "Surface", GameObjectsUtil.calculateBox(mainModel, "Surface"), 0f));
        constructors.put("Castle-1", new Constructor(mainModel, "Castle-1", GameObjectsUtil.calculateBox(mainModel, "Castle-1"), 0f));
        constructors.put("Unit-1", new Constructor(mainModel, "Unit-1", GameObjectsUtil.calculateSphere(mainModel, "Unit-1"), 1f));
        return constructors;
    }

    public List<GameObject> constructMainObjects() {
        return Arrays.asList(constructStaticObject("Surface"),
                constructStaticObject("Castle-1"),
                constructObject("Unit-1"));
    }

    public GameObject constructStaticObject(String value) {
        GameObject object = this.getGameObjectsConstructors().get(value).construct();
        object.body.setCollisionFlags(object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        object.body.setContactCallbackFlag(GROUND_FLAG);
        object.body.setContactCallbackFilter(0);
        object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        return object;
    }

    public GameObject constructObject(String value) {
        GameObject obj = this.getGameObjectsConstructors().get(value).construct();
        obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.body.setContactCallbackFlag(OBJECT_FLAG);
        obj.body.setContactCallbackFilter(GROUND_FLAG);
        return obj;
    }

    public void dispose() {
        for (GameObject.Constructor ctor : constructorsArrayMap.values()) {
            ctor.dispose();
        }
        constructorsArrayMap.clear();
        mainModel.dispose();
    }

    public ArrayMap<String, GameObject.Constructor> getGameObjectsConstructors() {
        return constructorsArrayMap;
    }
}
