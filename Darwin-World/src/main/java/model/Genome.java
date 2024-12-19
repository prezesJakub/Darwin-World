package model;

import information.GenomeSpecification;

import java.util.Random;

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

    private void generateGenes() {
        Random random = new Random();
        for (int i = 0; i < this.length; i++) {
            genes[i] = random.nextInt(8);
        }
    }
    public int getLength() {
        return this.length;
    }
    public int[] getGenes() {
        return this.genes;
    }
}
