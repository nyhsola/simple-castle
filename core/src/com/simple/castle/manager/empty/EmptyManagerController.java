package com.simple.castle.manager.empty;

import com.simple.castle.manager.BlockScene;
import com.simple.castle.manager.ChangeScene;
import com.simple.castle.manager.ManagerController;

public class EmptyManagerController implements ManagerController {

    @Override
    public ChangeScene getChangeScene() {
        return new EmptyChangeScene();
    }

    @Override
    public BlockScene getBlockScene() {
        return new EmptyBlockScene();
    }

}
