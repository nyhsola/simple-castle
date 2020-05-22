package com.simple.castle.server.tcp;

import com.simple.castle.base.ModelSend;

import java.util.List;

public interface DataListener {
    void worldTick(List<ModelSend> modelSendList);
}
