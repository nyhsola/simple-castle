package com.simple.castle.object.constructors;

import java.util.Map;

public class GameModelJson {
    private String model;
    private String shape;
    private Integer mass;

    public GameModelJson(String model, String shape, Integer mass) {
        this.model = model;
        this.shape = shape;
        this.mass = mass;
    }

    public GameModelJson(Map<String, Object> values) {
        this.model = (String) values.get("model");
        this.shape = (String) values.get("shape");
        this.mass = (Integer) values.get("mass");
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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
}
