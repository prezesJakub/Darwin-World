package maps;

import information.AnimalSpecification;
import information.MapSpecification;
import model.MapChangeListener;
import model.MapDirection;
import model.Vector2d;
import objects.Animal;

import java.util.*;

public class AbstractWorldMap implements WorldMap {
 //   private List<Animal> animalList = new ArrayList<>();
    protected final Map<Vector2d, List<Animal>> animals = Collections.synchronizedMap(new HashMap<>());
    protected final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final MapSpecification mapSpec;

    public AbstractWorldMap(MapSpecification mapSpec) {
        this.mapSpec = mapSpec;
    }

    public void generateAnimals(int numberOfAnimals, AnimalSpecification animalSpec) {
        for(int i=0; i<numberOfAnimals; i++) {
            Vector2d position = new Vector2d((int) (Math.random() * (mapSpec.mapWidth())),
                    (int) (Math.random() * (mapSpec.mapHeight())));
            Animal animal = new Animal(position, animalSpec);
            place(animal);
        }
        mapChanged("Generated animals");
    }

    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(canMoveTo(position)) {
            if(animals.containsKey(position)) {
                List<Animal> animalList = animals.get(position);
                animalList.add(animal);
            } else {
                List<Animal> animalList = new ArrayList<>();
                animalList.add(animal);
                animals.put(position, animalList);
            }
            mapChanged("Animal placed at " + position);
        }
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
   //     System.out.println(animal.getActiveGene() + " " + animal.getPosition());
        animal.move(direction, this);
        List<Animal> animalList = animals.get(oldPosition);
        animalList.remove(animal);
        if(animalList.isEmpty()) {
            animals.remove(oldPosition);
        }
        List<Animal> newAnimalList = animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>());
        newAnimalList.add(animal);
    }

    public void moveAnimals() {
        Map<Vector2d, List<Animal>> animalsMap = copyAnimalsMap();
        for(Map.Entry<Vector2d, List<Animal>> entry : animalsMap.entrySet()) {
            List<Animal> animalList = entry.getValue();

            for(Animal animal : animalList) {
                int actualGene = animal.getActiveGene();
                animal.rotate(MapDirection.fromInt(actualGene));
                move(animal, animal.getOrientation());
            }
        }
        mapChanged("Moved animals");
    }

    private Map<Vector2d, List<Animal>> copyAnimalsMap() {
        Map<Vector2d, List<Animal>> animalsMap = new HashMap<>();
        for(Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animalList = entry.getValue();
            List<Animal> animalListCopy = new ArrayList<>(animalList);
            animalsMap.put(position, animalListCopy);
        }
        return animalsMap;
    }

    public void endDay() {
        mapChanged("End day");
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return getElement(position) != null;
    }

    @Override
    public List<Animal> getElement(Vector2d position) {
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
        System.out.println(message);
        for(MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

 /*   @Override
    public List<Animal> getAnimals() {
        List<Animal> elements = new ArrayList<>(animals.values());
        return elements;
    }*/

    @Override
    public List<Vector2d> getElementPositions() {
        List<Vector2d> elementPositions = new ArrayList<>(animals.keySet());
        return elementPositions;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(new Vector2d(mapSpec.mapWidth()-1, mapSpec.mapHeight()-1)) &&
                position.follows(new Vector2d(0,0));
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
