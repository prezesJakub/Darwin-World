package simulation;

import information.AnimalSpecification;
import maps.WorldMap;

public class Simulation implements Runnable {
    private WorldMap map;
    private boolean running = true;


    public Simulation(WorldMap map, int startingAnimalsAmount, AnimalSpecification animalSpec) {
        this.map=map;
        map.generateAnimals(startingAnimalsAmount, animalSpec);
    }

    @Override
    public void run() {
        while (running) {
            map.moveAnimals();
            map.endDay();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Simulation interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopSimulation() {
        running = false;
    }
    public void unpauseSimulation() {
        running = true;
    }
}
