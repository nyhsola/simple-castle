package com.simple.castle.game.core.utils;

import com.simple.castle.server.game.core.object.unit.abs.AbstractGameObject;

import java.util.List;

public final class CastleListUtils {
    private CastleListUtils() {
    }

    public static AbstractGameObject getNextAvailable(List<AbstractGameObject> list, AbstractGameObject current) {
        int i = list.indexOf(current);
        if (i >= 0 && i < list.size() - 1) {
            i += 1;
        }
        return list.get(i);
    }
}
