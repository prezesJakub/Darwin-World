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
        while (running) {
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Simulation interrupted");
                Thread.currentThread().interrupt();
            }
            map.cleanDeadBodies();
            map.moveAnimals();
            map.sortAnimals();
            map.feedAnimals();
            //System.out.println(map.getPlantPositions().size());
            map.endDay();
        }
    }

    public void stopSimulation() {
        running = false;
    }
    public void unpauseSimulation() {
        running = true;
    }
}
