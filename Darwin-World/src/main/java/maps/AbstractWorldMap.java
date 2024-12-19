package maps;

import information.AnimalSpecification;
import information.MapSpecification;
import model.MapChangeListener;
import model.MapDirection;
import model.MapElement;
import model.Vector2d;
import objects.Animal;

import java.util.*;

public class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final MapSpecification mapSpec;

    public AbstractWorldMap(MapSpecification mapSpec) {
        this.mapSpec = mapSpec;
    }

    public void generateAnimals(int numberOfAnimals, AnimalSpecification animalSpec) {
        for(int i=0; i<numberOfAnimals; i++) {
            Vector2d position = new Vector2d((int) (Math.random() * (mapSpec.mapWidth()-1)),
                    (int) (Math.random() * (mapSpec.mapHeight()-1)));
            Animal animal = new Animal(position, animalSpec);
            animals.put(position, animal);
        }
        mapChanged("Generated animals");
    }

    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(canMoveTo(position)) {
            animals.put(position, animal);
            mapChanged("Animal placed at " + position);
        }
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        if(getElement(oldPosition) == animal) {
            //to do
        }
    }

    public void moveAnimals() {
        Map<Vector2d, Animal> newAnimalMap = new HashMap<>();

        animals.values().forEach(Animal -> {
            //to do
        });
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return getElement(position) != null;
    }

    @Override
    public MapElement getElement(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void mapChanged(String message) {
        for(MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public List<Animal> getAnimals() {
        List<Animal> elements = new ArrayList<>(animals.values());
        return elements;
    }

    @Override
    public List<Vector2d> getElementPositions() {
        List<Vector2d> elementPositions = new ArrayList<>(animals.keySet());
        return elementPositions;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return true; //do zrobienia
    }

    @Override
    public MapSpecification getMapSpec() {
        return this.mapSpec;
    }

    @Override
    public int getWidth() {
        return this.mapSpec.mapWidth();
    }

    @Override
    public int getHeight() {
        return this.mapSpec.mapHeight();
    }

    @Override
    public int getStartingPlantsAmount() {
        return this.mapSpec.startingPlantsAmount();
    }

    @Override
    public int getStartingAnimalsAmount() {
        return this.mapSpec.startingAnimalsAmount();
    }

    @Override
    public int getDailyPlantsGrowth() {
        return this.mapSpec.dailyPlantsGrowth();
    }
}
