package com.grooptown.snorkunking.service.engine.tile;

import com.grooptown.snorkunking.service.engine.grid.Panel;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public abstract class Tile implements Panel {
    public String toString() {
        return toAscii();
    }
}
