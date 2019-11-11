package com.grooptown.snorkunking.service.engine.tile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thibautdebroca on 08/11/2019.
 */
public class TileService {
    private final static Map<String, Tile> nameToTiles = new HashMap<>();
    static {
        nameToTiles.put("Ice", new IceTile());
        nameToTiles.put("Wall", new WallTile());
        nameToTiles.put("Wll", new WallTile());
        nameToTiles.put("Box", new WoodBoxTile());
    }

    public static Tile getTile(String name) {
        return nameToTiles.get(name);
    }
}
