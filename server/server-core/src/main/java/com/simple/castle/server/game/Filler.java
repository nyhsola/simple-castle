package com.simple.castle.server.game;

import com.badlogic.gdx.math.Vector3;
import com.simple.castle.base.Position;
import com.simple.castle.base.World;
import com.simple.castle.server.physic.GroundObject;

public class Filler {

    public World fillWorld(World world, GroundObject groundObject) {
        Position groundPosition = world.getGround().getPosition();
        Vector3 position = groundObject.getPosition();

        groundPosition.setX(position.x);
        groundPosition.setY(position.y);
        groundPosition.setZ(position.z);

        return world;
    }

}
