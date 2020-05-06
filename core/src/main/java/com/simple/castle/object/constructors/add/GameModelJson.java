package com.simple.castle.object.constructors.add;

import java.util.Map;

public class GameModelJson {
    private String nodesPattern;
    private String shape;
    private Integer mass;
    private String interact;

    public GameModelJson(String nodesPattern, String shape, Integer mass, String interact) {
        this.nodesPattern = nodesPattern;
        this.shape = shape;
        this.mass = mass;
        this.interact = interact;
    }

    public GameModelJson(Map<String, Object> values) {
        this.nodesPattern = (String) values.get("nodes-pattern");
        this.shape = (String) values.get("shape");
        this.mass = (Integer) values.get("mass");
        this.interact = (String) values.get("interact");
    }

    public String getNodesPattern() {
        return nodesPattern;
    }

    public void setNodesPattern(String nodesPattern) {
        this.nodesPattern = nodesPattern;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getMass() {
        return mass;
    }

    public void setMass(Integer mass) {
        this.mass = mass;
    }

    public String getInteract() {
        return interact;
    }

    public void setInteract(String interact) {
        this.interact = interact;
    }
}
