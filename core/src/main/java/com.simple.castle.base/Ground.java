package com.simple.castle.base;

import java.io.Serializable;

public class Ground implements Serializable {
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
