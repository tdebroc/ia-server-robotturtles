package com.grooptown.snorkunking.service.engine.tile;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.grooptown.snorkunking.service.engine.grid.Panel;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class Tile implements Panel {
    public String toString() {
        return toAscii();
    }
}
