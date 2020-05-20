package com.simple.castle.server.tcp;

import com.simple.castle.base.World;

public interface DataListener {
    void worldTick(World world);
}
