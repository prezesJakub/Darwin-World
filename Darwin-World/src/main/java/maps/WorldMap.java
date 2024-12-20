package maps;

import information.AnimalSpecification;
import information.MapSpecification;
import model.*;
import objects.Animal;

import java.util.List;
import java.util.UUID;

public interface WorldMap extends MoveValidator {
    void generateAnimals(int numberOfAnimals, AnimalSpecification animalSpec);
    void place(Animal animal);
    void move(Animal animal, MapDirection direction);
    void moveAnimals();
    void endDay();
    boolean isOccupied(Vector2d position);
    List<Animal> getElement(Vector2d position);
    void addObserver(MapChangeListener observer);
    void removeObserver(MapChangeListener observer);
    UUID getId();
 //   List<Animal> getAnimals();
    List<Vector2d> getElementPositions();
    boolean canMoveTo(Vector2d position);
    MapSpecification getMapSpec();
    int getWidth();
    int getHeight();
    int getStartingPlantsAmount();
    int getStartingAnimalsAmount();
    int getDailyPlantsGrowth();
}
