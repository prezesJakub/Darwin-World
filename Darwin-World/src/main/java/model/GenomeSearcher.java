package model;

import objects.Animal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenomeSearcher {
    private static Genome bestGenome;
    private static int maxCount;

    public static void calculateMostPopularGenome(List<Animal> animals) {
        Map<Genome, Integer> genomePopulation = new HashMap<>();
        for (Animal animal : animals) {
            if(genomePopulation.containsKey(animal.getGenome())) {
                genomePopulation.put(animal.getGenome(), genomePopulation.get(animal.getGenome()) + 1);
            } else {
                genomePopulation.put(animal.getGenome(), 1);
            }
        }
        findMostPopularGenome(genomePopulation);
    }

    private static void findMostPopularGenome(Map<Genome, Integer> genomes) {
        maxCount = 0;
        bestGenome = null;
        for(Map.Entry<Genome, Integer> entry : genomes.entrySet()) {
            Genome actualGenome = entry.getKey();
            int actualCount = entry.getValue();
            if(actualCount > maxCount) {
                maxCount = actualCount;
                bestGenome = actualGenome;
            }
        }
    }

    public static Genome getBestGenome() {
        return bestGenome;
    }
    public static int getMaxCount() {
        return maxCount;
    }
}
