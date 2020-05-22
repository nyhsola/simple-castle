package com.simple.castle.server.tcp;

import com.simple.castle.base.ServerRespond;

public interface DataListener {
    void worldTick(ServerRespond serverRespond);
}
