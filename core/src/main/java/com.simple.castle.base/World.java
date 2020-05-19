package com.simple.castle.base;

import java.io.Serializable;

public class World implements Serializable {
    private static final long serialVersionUID = 1;

    private final Ground ground;

    public World() {
        ground = new Ground();
    }

    public World(World world) {
        ground = new Ground();
        Position otherGroundPosition = world.getGround().getPosition();
        Position groundPosition = ground.getPosition();

        groundPosition.setX(otherGroundPosition.getX());
        groundPosition.setY(otherGroundPosition.getY());
        groundPosition.setZ(otherGroundPosition.getZ());
    }

    public Ground getGround() {
        return ground;
    }

    @Override
    public String toString() {
        return "World{" +
                "ground=" + ground +
                '}';
    }
}
