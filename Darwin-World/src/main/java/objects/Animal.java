package objects;

import information.AnimalSpecification;
import model.*;

import java.rmi.server.UID;
import java.util.Arrays;
import java.util.Random;

public class Animal implements MapElement {
    private final AnimalSpecification spec;
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private int age = 0;
    private int childrenCount = 0;
    private int descendantsCount = 0;
    private int plantsEatenCount = 0;
    private Animal firstParent = null;
    private Animal secondParent = null;
    private final UID id = new UID();

    private Genome genome;
    private int activeGeneId = 0;

    public Animal(Vector2d position, AnimalSpecification spec) {
        Random random = new Random();
        this.spec = spec;
        this.orientation = MapDirection.fromInt(new Random().nextInt(8));
        this.position = position;
        this.energy = spec.startingEnergy();
        this.genome = new Genome(spec.genomeSpec());
    }

    public Animal(Animal parent1, Animal parent2) {
        this.spec = parent1.spec;
        this.orientation = MapDirection.fromInt(new Random().nextInt(8));
        this.position = parent1.position;
        this.energy = 2 * spec.reproductionCost();
        this.genome = new Genome(parent1, parent2);
        this.firstParent = parent1;
        this.secondParent = parent2;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
    public MapDirection getOrientation() {
        return this.orientation;
    }
    public int getEnergy() {
        return this.energy;
    }
    public int getAge() {
        return this.age;
    }
    public int getChildrenCount() {
        return this.childrenCount;
    }
    public int getDescendantsCount() {
        return this.descendantsCount;
    }
    public int getPlantsEatenCount() {
        return this.plantsEatenCount;
    }
    public Genome getGenome() {
        return this.genome;
    }
    public int getActiveGeneId() {
        return this.activeGeneId;
    }
    public int getActiveGene() {
        int[] genes = this.genome.getGenes();
        return genes[this.activeGeneId];
    }
    public UID getId() {
        return this.id;
    }
    public String toString() {
        return this.orientation.toString();
        //return String.valueOf(this.energy);
        //return Arrays.toString(this.genome.getGenes());
    }
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }
    public void eat() {
        this.plantsEatenCount++;
        this.energy += spec.energyFromEating();
    }
    public boolean canReproduce() {
        return this.energy >= spec.reproductionMinEnergy();
    }
    private void consumeEnergy(int consumedEnergy) {
        this.energy -= consumedEnergy;
    }
    public void nextDay() {
        this.age++;
        this.consumeEnergy(1);
        this.activeGeneId = (this.activeGeneId + 1) % genome.getLength();
    }
    public boolean isDead() {
        return this.energy <= 0;
    }
    public Animal reproduce(Animal partner) {
        Animal child = new Animal(this, partner);
        consumeEnergy(spec.reproductionCost());
        partner.consumeEnergy(spec.reproductionCost());
        this.childrenCount++;
        partner.childrenCount++;
    /*    System.out.println(Arrays.toString(genome.getGenes()) + " " +
                Arrays.toString(partner.genome.getGenes()) + " " + Arrays.toString(child.genome.getGenes()));*/
        return child;
    }
    public void rotate(MapDirection direction) {
        this.orientation = MapDirection.fromInt((this.orientation.toInt()+direction.toInt()) % 8);
    }
    public void move(MapDirection direction, MoveValidator validator) {
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        if(validator.canMoveTo(newPosition)) {
            this.position = newPosition;
        }
        else {
            this.orientation = MapDirection.fromInt((this.orientation.toInt()+4) % 8);
        }
        nextDay();
    }
}
