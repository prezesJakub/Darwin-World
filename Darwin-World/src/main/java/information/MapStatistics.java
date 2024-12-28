package information;

import maps.WorldMap;
import model.Genome;
import model.GenomeSearcher;
import model.Vector2d;
import objects.Animal;
import objects.Grass;

import java.util.List;
import java.util.Map;

public class MapStatistics {
    private int day;
    private int aliveAnimals;
    private int allAnimalCount;
    private int plantCount;
    private int freeTiles;
    private String mostPopularGenomeDetails;
    private Genome mostPopularGenome = null;
    private int mostPopularGenomeCount = 0;
    private double averageEnergy;
    private double averageLifetime;
    private double averageChildrenAmount;

    public void updateStats(WorldMap map, List<Animal> animals, List<Animal> deadAnimals,
                            Map<Vector2d, Grass> plants, Map<Vector2d, List<Animal>> animalMap) {
        nextDay();
        countAnimals(animals, deadAnimals);
        countPlants(plants);
        countFreeTiles(map, plants, animalMap);
        calculateMostPopularGenome(animals);
        calculateAverageEnergy(animals);
        calculateAverageLifetime(deadAnimals);
        calculateAverageChildrenAmount(animals);
    }

    private void nextDay() {
        day++;
    }
    private void countAnimals(List<Animal> animals, List<Animal> deadAnimals) {
        aliveAnimals = animals.size();
        allAnimalCount = animals.size() + deadAnimals.size();
    }
    private void countPlants(Map<Vector2d, Grass> plants) {
        plantCount=plants.size();
    }
    private void countFreeTiles(WorldMap map, Map<Vector2d, Grass> plants, Map<Vector2d, List<Animal>> animals) {
        freeTiles=0;
        for(int i=0; i<map.getWidth(); i++) {
            for(int j=0; j<map.getHeight(); j++) {
                Vector2d pos = new Vector2d(i, j);
                if(!plants.containsKey(pos) && !animals.containsKey(pos) && !map.isWater(pos)) {
                    freeTiles++;
                }
            }
        }
    }
    private void calculateMostPopularGenome(List<Animal> animals) {
        if (animals.isEmpty()) {
            mostPopularGenome = null;
            mostPopularGenomeCount = 0;
            mostPopularGenomeDetails = "null";
            return;
        }
        GenomeSearcher.calculateMostPopularGenome(animals);
        mostPopularGenome = GenomeSearcher.getBestGenome();
        mostPopularGenomeCount = GenomeSearcher.getMaxCount();
        mostPopularGenomeDetails = mostPopularGenome.toString() + " x" + String.valueOf(mostPopularGenomeCount);
    }
    private void calculateAverageEnergy(List<Animal> animals) {
        int energySum = 0;
        int animalCount = animals.size();
        if(animalCount == 0) {
            averageEnergy = 0;
            return;
        }

        for(Animal animal : animals) {
            energySum += animal.getEnergy();
        }
        averageEnergy = (double) energySum / animalCount;
        averageEnergy *= 100;
        averageEnergy = Math.round(averageEnergy);
        averageEnergy /= 100;
    }

    private void calculateAverageLifetime(List<Animal> deadAnimals) {
        int lifetimeSum = 0;
        int animalCount = deadAnimals.size();
        if(animalCount == 0) {
            averageLifetime = 0;
            return;
        }

        for(Animal animal : deadAnimals) {
            lifetimeSum += animal.getAge();
        }
        averageLifetime = (double) lifetimeSum / animalCount;
        averageLifetime *= 100;
        averageLifetime = Math.round(averageLifetime);
        averageLifetime /= 100;
    }

    private void calculateAverageChildrenAmount(List<Animal> animals) {
        int childrenAmountSum = 0;
        int animalCount = animals.size();
        if(animalCount == 0) {
            averageChildrenAmount = 0;
            return;
        }
        for(Animal animal : animals) {
            childrenAmountSum += animal.getChildrenCount();
        }
        averageChildrenAmount = (double) childrenAmountSum / animalCount;
        averageChildrenAmount *= 100;
        averageChildrenAmount = Math.round(averageChildrenAmount);
        averageChildrenAmount /= 100;
    }

    public String[] getStatsTable() {
        return new String[]{
                String.valueOf(day),
                String.valueOf(aliveAnimals),
                String.valueOf(allAnimalCount),
                String.valueOf(plantCount),
                String.valueOf(freeTiles),
                mostPopularGenomeDetails,
                String.valueOf(averageEnergy),
                String.valueOf(averageLifetime),
                String.valueOf(averageChildrenAmount)
        };
    }
    public int getDay() {
        return this.day;
    }
    public int getAliveAnimals() {
        return this.aliveAnimals;
    }
    public int getAllAnimalCount() {
        return this.allAnimalCount;
    }
    public int getPlantCount() {
        return this.plantCount;
    }
    public int getFreeTiles() {
        return this.freeTiles;
    }
    public Genome getMostPopularGenome() {
        return this.mostPopularGenome;
    }
    public String getMostPopularGenomeDetails() {
        return this.mostPopularGenomeDetails;
    }
    public double getAverageEnergy() {
        return this.averageEnergy;
    }
    public double getAverageLifetime() {
        return this.averageLifetime;
    }
    public double getAverageChildrenAmount() {
        return this.averageChildrenAmount;
    }
}
