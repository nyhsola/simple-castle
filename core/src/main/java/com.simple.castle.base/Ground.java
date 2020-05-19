package com.simple.castle.base;

public class Ground {
    private final Position position = new Position();

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Ground{" +
                "position=" + position +
                '}';
    }
}
