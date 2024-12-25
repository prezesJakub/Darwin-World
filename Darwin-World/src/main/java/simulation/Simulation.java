package simulation;

import information.AnimalSpecification;
import maps.WorldMap;

public class Simulation implements Runnable {
    private WorldMap map;
    private boolean running = true;


    public Simulation(WorldMap map, AnimalSpecification animalSpec) {
        this.map=map;
        map.generateAnimals(map.getMapSpec().startingAnimalsAmount(), animalSpec);
        map.generatePlants(map.getMapSpec().startingPlantsAmount());
    }

    @Override
    public void run() {
        while (true) {
            if (running) {
                map.cleanDeadBodies();
                map.moveAnimals();
                map.sortAnimals();
                map.feedAnimals();
                map.reproduceAnimals();
                map.endDay();
            }
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Simulation interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void changeStateOfSimulation() {
        running = !running;
    }
}
