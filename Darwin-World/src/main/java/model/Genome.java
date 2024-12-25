package model;

import information.GenomeSpecification;
import objects.Animal;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Genome {
    private final int length;
    private int[] genes;
    private final GenomeSpecification genomeSpec;

    public Genome(GenomeSpecification genomeSpec) {
        this.genomeSpec = genomeSpec;
        this.length = this.genomeSpec.genomeLength();
        this.genes = new int[this.length];
        generateGenes();
    }

    public Genome(Animal animal1, Animal animal2) {
        this.genomeSpec=animal1.getGenome().genomeSpec;
        this.length = this.genomeSpec.genomeLength();
        this.genes = new int[this.length];
        inheritGenes(animal1, animal2);
        mutate(this);
    }

    private void generateGenes() {
        Random random = new Random();
        for (int i = 0; i < this.length; i++) {
            genes[i] = random.nextInt(8);
        }
    }

    private void inheritGenes(Animal animal1, Animal animal2) {
        Random random = new Random();
        boolean inheritSide = random.nextBoolean();
        int sumEnergy = animal1.getEnergy() + animal2.getEnergy();
        if (inheritSide) {
            combineGenes(animal1, animal2, sumEnergy);
        } else {
            combineGenes(animal2, animal1, sumEnergy);
        }
    }

    private void combineGenes(Animal animal1, Animal animal2, int sumEnergy) {
        int genesBound = Math.round((float) (animal1.getEnergy() * length) / sumEnergy);
        for(int i = 0; i < genesBound; i++) {
            genes[i] = animal1.getGenome().getGenes()[i];
        }
        for(int i=genesBound; i < length; i++) {
            genes[i] = animal2.getGenome().getGenes()[i];
        }
    }

    private void mutate(Genome genome) {
        if(genome.genomeSpec.mutationType() == 0) {
            randomMutation(genome);
        }
    }

    private void randomMutation(Genome genome) {
        Random random = new Random();
        int maxMutations = Math.min(genome.genomeSpec.maxMutations(), genome.length);
        int minMutations = Math.min(genome.genomeSpec.minMutations(), maxMutations);

        int mutationCount = random.nextInt(maxMutations-minMutations+1) + minMutations;
        Set<Integer> mutationPositions = new HashSet<>();
        while (mutationPositions.size() < mutationCount) {
            int position = random.nextInt(genome.length);
            mutationPositions.add(position);
        }

        for(int position : mutationPositions) {
            int newValue = random.nextInt(8);
            genome.getGenes()[position] = newValue;
        }
    }
    public int getLength() {
        return this.length;
    }
    public int[] getGenes() {
        return this.genes;
    }
}
