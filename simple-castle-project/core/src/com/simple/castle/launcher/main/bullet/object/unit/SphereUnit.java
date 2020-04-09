package com.simple.castle.launcher.main.bullet.object.unit;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.utils.GameObjectsUtil;

import static com.simple.castle.launcher.main.bullet.main.ModelFactory.GROUND_FLAG;
import static com.simple.castle.launcher.main.bullet.main.ModelFactory.OBJECT_FLAG;

public class SphereUnit extends GameObject {

    private static final String UNIT_1 = "Unit-1";
    private static final float MASS = 1f;

    public SphereUnit(Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(model, node, constructionInfo);

        this.body.setCollisionFlags(this.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        this.body.setContactCallbackFlag(OBJECT_FLAG);
        this.body.setContactCallbackFilter(GROUND_FLAG);
    }

    public static class Builder {
        private final GameObjectConstructor sphereConstructor;

        public Builder(Model model) {
            BoundingBox boundingBox = model.getNode(UNIT_1).calculateBoundingBox(new BoundingBox());
            btSphereShape shape = GameObjectsUtil.calculateSphere(boundingBox);
            sphereConstructor = new GameObjectConstructor(model, UNIT_1, shape, MASS);
        }

        public SphereUnit build() {
            return new SphereUnit(sphereConstructor.model, sphereConstructor.node, sphereConstructor.constructionInfo);
        }
    }
}
