package maps;

import information.AnimalSpecification;
import information.MapSpecification;
import information.MapStatistics;
import model.*;
import objects.Animal;
import objects.Grass;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WorldMap extends MoveValidator {
    void generateAnimals(int numberOfAnimals, AnimalSpecification animalSpec);
    void generatePlants(int numberOfPlants);
    void generateTiles();
    void place(Animal animal);
    void move(Animal animal, MapDirection direction);
    void moveAnimals();
    void endDay();
    void sortAnimals();
    void feedAnimals();
    void cleanDeadBodies();
    void reproduceAnimals();
    boolean isOccupied(Vector2d position);
    List<Animal> getAnimal(Vector2d position);
    Grass getPlant(Vector2d position);
    void addObserver(MapChangeListener observer);
    void removeObserver(MapChangeListener observer);
    UUID getId();
    List<Vector2d> getAnimalPositions();
    List<Vector2d> getPlantPositions();
    Map<Vector2d, TileType> getTiles();
    boolean isWater(Vector2d position);
    boolean canMoveTo(Vector2d position);
    MapSpecification getMapSpec();
    int getWidth();
    int getHeight();
    int getStartingPlantsAmount();
    int getStartingAnimalsAmount();
    int getDailyPlantsGrowth();
    MapStatistics getMapStats();
}
