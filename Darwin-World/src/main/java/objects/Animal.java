package objects;

import information.AnimalSpecification;
import model.*;

import java.rmi.server.UID;
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
        consumeEnergy(spec.reproductionCost());
        //to do
        return this;
    }
    public void rotate(MapDirection direction) {
        this.orientation = MapDirection.fromInt((this.orientation.toInt()+direction.toInt()) % 8);
        this.activeGeneId = (this.activeGeneId + 1) % genome.getLength();
    }
    public void move(MapDirection direction, MoveValidator validator) {
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        if(validator.canMoveTo(newPosition)) {
            this.position = newPosition;
        }
    }

    //to do
}
