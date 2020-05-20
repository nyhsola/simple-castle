package com.simple.castle.server.physic;

interface Flags {
    short GROUND_FLAG = 1 << 8;
    short OBJECT_FLAG = 1 << 9;
    short ALL_FLAG = -1;
}
