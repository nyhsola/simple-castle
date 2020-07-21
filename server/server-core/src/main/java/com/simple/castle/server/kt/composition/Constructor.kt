package com.simple.castle.server.composition;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.simple.castle.server.composition.add.InteractType;
import com.simple.castle.server.composition.add.PhysicShape;
import com.simple.castle.server.physic.unit.PhysicObject;

public class Constructor {
    private final Model model;
    private final String id;
    private final InteractType interactType;
    private final PhysicShape physicShape;
    private final Boolean instantiate;
    private final Boolean hide;

    public Constructor(Model model, String id, InteractType interactType, PhysicShape physicShape, Boolean instantiate, Boolean hide) {
        this.model = model;
        this.id = id;
        this.interactType = interactType;
        this.physicShape = physicShape;
        this.instantiate = instantiate;
        this.hide = hide;
    }

    public PhysicObject buildPhysic() {
        Node node = model.getNode(id);
        btCollisionShape shape = physicShape.build(node);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(0, null, shape);
        return interactType.build(info);
    }

    public ModelInstance buildModel() {
        return new ModelInstance(model, id, true);
    }

    public String getId() {
        return id;
    }

    public Boolean getInstantiate() {
        return instantiate;
    }

    public Boolean getHide() {
        return hide;
    }
}
