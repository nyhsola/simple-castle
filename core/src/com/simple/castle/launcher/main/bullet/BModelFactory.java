package com.simple.castle.launcher.main.bullet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.object.BGameObject;
import com.simple.castle.launcher.main.bullet.object.BGameObject.Constructor;
import com.simple.castle.launcher.main.bullet.object.BGameObjectsUtil;
import com.simple.castle.launcher.main.utils.BModelLoader;

import java.util.Arrays;
import java.util.List;

public class BModelFactory extends ApplicationAdapter {
    private final static short GROUND_FLAG = 1 << 8;
    private final static short OBJECT_FLAG = 1 << 9;
    private final static short ALL_FLAG = -1;

    private ArrayMap<String, BGameObject.Constructor> constructorArrayMap;
    private Model model;

    @Override
    public void create() {
        constructorArrayMap = constructObjects();
    }

    public List<BGameObject> constructMap() {
        BGameObject cube1 = constructStaticObject("Cube1");


        Node cubeNode = cube1.getNode("Cube1");
//        cube1.transform.set(cubeNode.globalTransform);

//        cube1.motionState.setWorldTransform(cube1.transform);
//        cube1.body.translate(new Vector3(10, 50 , 0));
//        cube1.transform.translate(new Vector3(10, 0 , 0));
//        cube1.transform.trn(0, 0, 0);
//        cube1.body.proceedToTransform(cube1.transform);

        return Arrays.asList(constructGround(), cube1);
    }

    public BGameObject constructStaticObject(String value) {
        BGameObject object = this.getGameObjects().get(value).construct();
        object.body.setCollisionFlags(object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        object.body.setContactCallbackFlag(GROUND_FLAG);
        object.body.setContactCallbackFilter(0);
        object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        return object;
    }

    public BGameObject constructGround() {
        BGameObject object = this.getGameObjects().get("ground").construct();
        object.body.setCollisionFlags(object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        object.body.setContactCallbackFlag(GROUND_FLAG);
        object.body.setContactCallbackFilter(0);
        object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        return object;
    }

    public BGameObject randomObject(int objCount) {
        BGameObject obj = this.getGameObjects().values[1 + MathUtils.random(this.getGameObjects().size - 2)].construct();
        obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
        obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f));
        obj.body.proceedToTransform(obj.transform);
        obj.body.setUserValue(objCount);
        obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.body.setContactCallbackFlag(OBJECT_FLAG);
        obj.body.setContactCallbackFilter(GROUND_FLAG);
        return obj;
    }

    private ArrayMap<String, BGameObject.Constructor> constructObjects() {
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("ground", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(5f, 1f, 5f);
        mb.node().id = "sphere";
        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
                .sphere(1f, 1f, 1f, 10, 10);
        mb.node().id = "box";
        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
                .box(1f, 1f, 1f);
        mb.node().id = "cone";
        mb.part("cone", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .cone(1f, 2f, 1f, 10);
        mb.node().id = "capsule";
        mb.part("capsule", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .capsule(0.5f, 2f, 10);
        mb.node().id = "cylinder";
        mb.part("cylinder", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10);
        model = mb.end();

        Model mapModel = BModelLoader.loadModel();


        ArrayMap<String, Constructor> constructors;
        constructors = new ArrayMap<>(String.class, Constructor.class);
        constructors.put("ground", new Constructor(mapModel, "Plane", BGameObjectsUtil.calculateBox(mapModel, "Plane"), 0f));
        constructors.put("Cube1", new Constructor(mapModel, "Cube1", BGameObjectsUtil.calculateBox(mapModel, "Cube1"), 1f));

//        constructors.put("sphere", new BGameObject.Constructor(model, "sphere", new btSphereShape(0.5f), 1f));
//        constructors.put("box", new BGameObject.Constructor(model, "box", new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)), 1f));
//        constructors.put("cone", new BGameObject.Constructor(model, "cone", new btConeShape(0.5f, 2f), 1f));
//        constructors.put("capsule", new BGameObject.Constructor(model, "capsule", new btCapsuleShape(.5f, 1f), 1f));
//        constructors.put("cylinder", new Constructor(model, "cylinder", new btCylinderShape(new Vector3(.5f, 1f, .5f)), 1f));
//        constructors.put("myground", new BGameObject.Constructor(mapModel, "Cube", new btSphereShape(0.5f), 1f));

        return constructors;
    }

    public void dispose() {
        for (BGameObject.Constructor ctor : constructorArrayMap.values()) {
            ctor.dispose();
        }
        constructorArrayMap.clear();
        model.dispose();
    }

    public ArrayMap<String, BGameObject.Constructor> getGameObjects() {
        return constructorArrayMap;
    }
}
