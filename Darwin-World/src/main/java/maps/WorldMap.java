package maps;

import information.AnimalSpecification;
import information.MapSpecification;
import model.MapChangeListener;
import model.MapDirection;
import model.MapElement;
import model.Vector2d;
import objects.Animal;

import java.util.List;
import java.util.UUID;

public interface WorldMap {
    void generateAnimals(int numberOfAnimals, AnimalSpecification animalSpec);
    void place(Animal animal);
    void move(Animal animal, MapDirection direction);
    void moveAnimals();
    boolean isOccupied(Vector2d position);
    MapElement getElement(Vector2d position);
    void addObserver(MapChangeListener observer);
    void removeObserver(MapChangeListener observer);
    UUID getId();
    List<Animal> getAnimals();
    List<Vector2d> getElementPositions();
    boolean canMoveTo(Vector2d position);
    MapSpecification getMapSpec();
    int getWidth();
    int getHeight();
    int getStartingPlantsAmount();
    int getStartingAnimalsAmount();
    int getDailyPlantsGrowth();
}
