package objects;

import information.AnimalSpecification;
import model.*;

import java.rmi.server.UID;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Animal implements MapElement {
    private final AnimalSpecification spec;
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private int age = 0;
    private int childrenCount = 0;
    private int descendantsCount = 0;
    private int plantsEatenCount = 0;
    private Integer deathDay = null;
    private Animal firstParent = null;
    private Animal secondParent = null;
    private final UID id;

    private Genome genome;
    private int activeGeneId = 0;

    public Animal(Vector2d position, AnimalSpecification spec) {
        Random random = new Random();
        this.spec = spec;
        this.orientation = MapDirection.fromInt(new Random().nextInt(8));
        this.position = position;
        this.energy = spec.startingEnergy();
        this.genome = new Genome(spec.genomeSpec());
        this.id=new UID();
    }

    public Animal(Animal parent1, Animal parent2) {
        this.spec = parent1.spec;
        this.orientation = MapDirection.fromInt(new Random().nextInt(8));
        this.position = parent1.position;
        this.energy = 2 * spec.reproductionCost();
        this.genome = new Genome(parent1, parent2);
        this.firstParent = parent1;
        this.secondParent = parent2;
        this.id=new UID();
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDeathDay(int deathDay) {
        this.deathDay = deathDay;
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
    public Animal getFirstParent() {
        return this.firstParent;
    }
    public Animal getSecondParent() {
        return this.secondParent;
    }
    public String[] getAnimalStats() {
        return new String[]{
                String.valueOf(this.id),
                String.valueOf(this.position),
                this.genome.toString(),
                String.valueOf(getActiveGene()),
                String.valueOf(this.energy),
                String.valueOf(this.plantsEatenCount),
                String.valueOf(this.childrenCount),
                String.valueOf(this.descendantsCount),
                String.valueOf(this.age),
                String.valueOf(this.deathDay)
        };
    }
    public String toString() {
        return "Id: " + this.id.toString() + " | Energia: " + String.valueOf(this.energy);
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
        updateDescendantsCount(child);
        return child;
    }
    private void updateDescendantsCount(Animal animal) {
        Set<Animal> processed = new HashSet<>();
        processed.add(animal);
        updateDescendantsCountRecursive(animal, processed);
    }

    private void updateDescendantsCountRecursive(Animal animal, Set<Animal> processed) {
        if(animal == null) {
            return;
        }

        Animal parentOne = animal.getFirstParent();
        Animal parentTwo = animal.getSecondParent();

        if(parentOne != null && !processed.contains(parentOne)) {
            parentOne.descendantsCount++;
            processed.add(parentOne);
            updateDescendantsCountRecursive(parentOne, processed);
        }
        if(parentTwo != null && !processed.contains(parentTwo)) {
            parentTwo.descendantsCount++;
            processed.add(parentTwo);
            updateDescendantsCountRecursive(parentTwo, processed);
        }
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

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null || getClass() != other.getClass()) return false;
        Animal animal = (Animal) other;
        return id.equals(animal.id);
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
