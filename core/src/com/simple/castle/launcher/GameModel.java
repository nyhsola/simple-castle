package com.simple.castle.launcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class GameModel extends ApplicationAdapter {
    private ModelInstance modelInstance;
    private btCollisionObject btCollisionObject;

    public GameModel(String nodeName, Model model) {
        modelInstance = new ModelInstance(model, nodeName);
        BoundingBox boundingBox = new BoundingBox();
        modelInstance.calculateBoundingBox(boundingBox);

        btCollisionObject = new btCollisionObject();
        Vector3 out = new Vector3();
        out = boundingBox.getDimensions(out);

        btBoxShape shape = new btBoxShape(new Vector3(out.x / 2, out.y / 2, out.z / 2));

        btCollisionObject.setCollisionShape(shape);
        btCollisionObject.setWorldTransform(modelInstance.transform);

        Gdx.app.log("", "" + out + " ");
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public btCollisionObject getBtCollisionObject() {
        return btCollisionObject;
    }

    @Override
    public void dispose() {
        btCollisionObject.dispose();
    }
}
