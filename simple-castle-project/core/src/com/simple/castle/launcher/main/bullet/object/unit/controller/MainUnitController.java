package com.simple.castle.launcher.main.bullet.object.unit.controller;

import com.simple.castle.launcher.main.bullet.object.unit.SphereUnit;

import java.util.ArrayList;
import java.util.List;

public class MainUnitController {

    private List<SphereUnit> sphereUnitList = new ArrayList<>();

    public void update() {
    }

    public void addUnit(SphereUnit sphereUnit) {
        sphereUnitList.add(sphereUnit);
    }
}
