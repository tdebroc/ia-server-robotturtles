package com.grooptown.snorkunking.service.engine.tile;


import com.grooptown.snorkunking.service.engine.grid.PanelEnum;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class IceTile extends Tile {
    @Override
    public String toAscii() {
        return "Ice ";
    }
    @Override
    public PanelEnum getPanelName() {
        return PanelEnum.ICE;
    }
}
