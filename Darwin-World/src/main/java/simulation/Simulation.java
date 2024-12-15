package simulation;

import maps.WorldMap;

public class Simulation implements Runnable {
    private WorldMap map;
    private boolean running = true;


    public Simulation(WorldMap map) {
        this.map = map;
    }

    @Override
    public void run() {
        // to do
    }
}
