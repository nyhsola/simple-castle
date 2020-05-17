package com.simple.castle.server.game.core.utils;

import com.badlogic.gdx.math.Vector3;

public final class CastleMathUtils {
    private CastleMathUtils() {

    }

    public static double getAngle(Vector3 a, Vector3 b) {
        Vector3 norA = b.cpy().nor();
        Vector3 norB = a.cpy().nor();
        float dot = norB.dot(norA);
        return Math.toDegrees(Math.acos(dot));
    }
}
