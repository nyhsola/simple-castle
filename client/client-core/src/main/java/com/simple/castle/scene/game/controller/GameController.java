package com.simple.castle.scene.game.controller;

import com.simple.castle.core.debug.DebugInformation;
import com.simple.castle.core.event.DoThen;
import com.simple.castle.core.manager.Controller;
import com.simple.castle.core.object.unit.abs.AbstractGameObject;

public class GameController implements Controller {

    private final DebugInformation debugInformation = new DebugInformation();
    private final DoThen doThen = new DoThen();
    private final PlayerController playerController;

    public GameController(PlayerController playerController) {
        this.playerController = playerController;

        doThen.then(playerController);
    }

    @Override
    public void update() {
        doThen.update();
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
