package maps;

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
            // do zrobienia
        }
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
    public List<MapElement> getElements() {
        List<MapElement> elements = new ArrayList<>(animals.values());
        return elements;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true; //do zrobienia
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
