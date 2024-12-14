package model;

import maps.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}
