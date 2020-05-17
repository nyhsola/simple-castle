package com.simple.castle.server.game.core.event;

import java.util.ArrayList;
import java.util.List;

public class DoThen {

    private final List<Done> chain = new ArrayList<>();
    private int index = 0;

    public void update() {
        if (index < chain.size()) {
            Done done = chain.get(index);
            done.update();
            if (done.isDone()) {
                index = index + 1;
            }
        }
    }

    public DoThen then(Done done) {
        chain.add(done);
        return this;
    }
}
