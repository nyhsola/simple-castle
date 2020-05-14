package com.simple.castle.scene.game.controller;

import com.simple.castle.core.debug.DebugInformation;
import com.simple.castle.core.manager.Controller;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;

public class GameController implements Controller {

    private final DebugInformation debugInformation = new DebugInformation();

    private final MapInitController mapInitController;
    private final PlayerController playerController;

    public GameController(MapInitController mapInitController, PlayerController playerController) {
        this.mapInitController = mapInitController;
        this.playerController = playerController;
    }

    @Override
    public void update() {
        playerController.update();
    }

    @Override
    public DebugInformation getDebugInfo() {
        debugInformation.setTimeLeft(playerController.getTimeLeft());
        debugInformation.setTotalUnits(playerController.getTotalUnits());
        return debugInformation;
    }

    @Override
    public void dispose() {
        playerController.dispose();
    }

    @Override
    public void collisionEvent(AbstractGameObject object1, AbstractGameObject object2) {
        playerController.collisionEvent(object1, object2);
    }
}
