package com.simple.castle.game.scenes.controller;

import com.simple.castle.server.game.core.debug.DebugInformation;
import com.simple.castle.server.game.core.event.DoThen;
import com.simple.castle.server.game.core.manager.Controller;
import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;

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
