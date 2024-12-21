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
            map.moveAnimals();
            map.endDay();
            map.generatePlants(map.getMapSpec().dailyPlantsGrowth());
        //    System.out.println(map.getPlantPositions());
           // System.out.println(map.getPlantPositions().size());
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
