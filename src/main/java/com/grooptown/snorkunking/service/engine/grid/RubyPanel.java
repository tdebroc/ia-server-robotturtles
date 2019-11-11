package com.grooptown.snorkunking.service.engine.grid;

/**
 * Created by thibautdebroca on 04/11/2019.
 */
public class RubyPanel implements Panel {

    @Override
    public String toAscii() {
        return "Ruby";
    }
    @Override
    public PanelEnum getPanelName() {
        return PanelEnum.RUBY;
    }
}
