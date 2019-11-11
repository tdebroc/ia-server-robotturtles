package com.grooptown.snorkunking.service.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautdebroca on 10/12/2017.
 */
public class Cell {

    private List<Chest> chests = new ArrayList<>();

    public List<Chest> getChests() {
        return chests;
    }

    public void setChests(List<Chest> chests) {
        this.chests = chests;
    }
}
