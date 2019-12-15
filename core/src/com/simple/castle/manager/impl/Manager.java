package com.simple.castle.manager.impl;

import java.util.List;
import java.util.Map;

public class Manager extends DefaultManager {

    public Manager(String startScene, Map<String, Scene> sceneMap) {
        super(startScene, sceneMap);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender) {
        super(startScene, sceneMap, alwaysRender);
    }

    public Manager(String startScene, Map<String, Scene> sceneMap, List<String> alwaysRender, List<String> blockInput) {
        super(startScene, sceneMap, alwaysRender, blockInput);
    }

}
