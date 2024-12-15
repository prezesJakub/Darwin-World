package maps;

import model.MapChangeListener;
import model.MapDirection;
import model.MapElement;
import model.Vector2d;
import objects.Animal;

import java.util.List;
import java.util.UUID;

public interface WorldMap {
    void place(Animal animal);
    void move(Animal animal, MapDirection direction);
    boolean isOccupied(Vector2d position);
    MapElement getElement(Vector2d position);
    void addObserver(MapChangeListener observer);
    void removeObserver(MapChangeListener observer);
    UUID getId();
    List<MapElement> getElements();
    boolean canMoveTo(Vector2d position);
    int getWidth();
    int getHeight();
    int getStartingPlantsAmount();
    int getStartingAnimalsAmount();
    int getDailyPlantsGrowth();
}
