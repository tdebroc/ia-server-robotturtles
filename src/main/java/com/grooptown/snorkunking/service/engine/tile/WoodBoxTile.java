package com.grooptown.snorkunking.service.engine.tile;

import com.grooptown.snorkunking.service.engine.grid.Panel;
import com.grooptown.snorkunking.service.engine.grid.PanelEnum;

/**
 * Created by thibautdebroca on 02/11/2019.
 */
public class WoodBoxTile extends Tile implements Panel {
    @Override
    public String toAscii() {
        return "WBox";
    }
    @Override
    public PanelEnum getPanelName() {
        return PanelEnum.WOODBOX;
    }
}
