package com.simple.castle.base;

import java.io.Serializable;

public class World implements Serializable {
    private static final long serialVersionUID = 1;

    private Ground ground;

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }
}
