package com.simple.castle.launcher.main.bullet.object;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;

public class BGameObjectsUtil {

    public static btBoxShape calculateBox(Model model, String node) {
        BoundingBox boundingBox = new BoundingBox();
        model.getNode(node).calculateBoundingBox(boundingBox);
        Vector3 dimensions = new Vector3();
        boundingBox.getDimensions(dimensions);
        return new btBoxShape(dimensions.scl(0.5f));
    }
}
