package model;

import information.GenomeSpecification;
import objects.Animal;

import java.util.Arrays;
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
        if(genome.genomeSpec.mutationType() == MutationType.COMPLETE_RANDOMNESS) {
            randomMutation(genome);
        }
        else if(genome.genomeSpec.mutationType() == MutationType.SLIGHT_CORRECTION) {
            slightCorrection(genome);
        }
    }

    private void randomMutation(Genome genome) {
        Random random = new Random();
        Set<Integer> mutationPositions = selectMutationPositions(genome);

        for(int position : mutationPositions) {
            int newValue = random.nextInt(8);
            genome.getGenes()[position] = newValue;
        }
    }

    private void slightCorrection(Genome genome) {
        Random random = new Random();
        Set<Integer> mutationPositions = selectMutationPositions(genome);

        for(int position : mutationPositions) {
            int actualValue = genome.getGenes()[position];
            int newValue = random.nextBoolean() ? (actualValue+1+8)%8 : (actualValue-1+8)%8;
            genome.getGenes()[position] = newValue;
        }
    }

    private Set<Integer> selectMutationPositions(Genome genome) {
        Random random = new Random();
        int maxMutations = Math.min(genome.genomeSpec.maxMutations(), genome.length);
        int minMutations = Math.min(genome.genomeSpec.minMutations(), maxMutations);

        int mutationCount = random.nextInt(maxMutations-minMutations+1) + minMutations;
        Set<Integer> mutationPositions = new HashSet<>();

        while (mutationPositions.size() < mutationCount) {
            int position = random.nextInt(genome.length);
            mutationPositions.add(position);
        }
        return mutationPositions;
    }

    public int getLength() {
        return this.length;
    }
    public int[] getGenes() {
        return this.genes;
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Genome other = (Genome) obj;
        return Arrays.equals(this.genes, other.genes);
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.genes);
    }
}
