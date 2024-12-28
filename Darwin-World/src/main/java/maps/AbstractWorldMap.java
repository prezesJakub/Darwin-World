package maps;

import information.AnimalSpecification;
import information.MapSpecification;
import information.MapStatistics;
import model.FoodGenerator;
import model.MapChangeListener;
import model.MapDirection;
import model.Vector2d;
import objects.Animal;
import objects.Grass;

import java.util.*;

public class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, List<Animal>> animals = Collections.synchronizedMap(new HashMap<>());
    protected final List<Animal> aliveAnimals = Collections.synchronizedList(new ArrayList<>());
    protected final List<Animal> deadAnimals = Collections.synchronizedList(new ArrayList<>());
    protected final Map<Vector2d, Grass> plants = Collections.synchronizedMap(new HashMap<>());
    protected final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final MapSpecification mapSpec;
    private final FoodGenerator foodGenerator;
    private final MapStatistics mapStats = new MapStatistics();

    public AbstractWorldMap(MapSpecification mapSpec) {
        this.mapSpec = mapSpec;
        this.foodGenerator = new FoodGenerator(mapSpec.bounds());
    }

    public void generateAnimals(int numberOfAnimals, AnimalSpecification animalSpec) {
        for(int i=0; i<numberOfAnimals; i++) {
            Vector2d position = new Vector2d((int) (Math.random() * (mapSpec.bounds().getWidth())),
                    (int) (Math.random() * (mapSpec.bounds().getHeight())));
            Animal animal = new Animal(position, animalSpec);
            place(animal);
        }
    }

    public void generatePlants(int numberOfPlants) {
        List<Vector2d> plantPositions = foodGenerator.generateFood(plants, numberOfPlants);
        for(Vector2d position : plantPositions) {
            Grass grass = new Grass(position);
            plants.put(position, grass);
        }
    }

    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(canMoveTo(position)) {
            aliveAnimals.add(animal);
            if(animals.containsKey(position)) {
                List<Animal> animalList = animals.get(position);
                animalList.add(animal);
            } else {
                List<Animal> animalList = new ArrayList<>();
                animalList.add(animal);
                animals.put(position, animalList);
            }
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
        Vector2d newPosition = animal.getPosition();
        newPosition = fixPosition(newPosition);
        animal.setPosition(newPosition);
        List<Animal> newAnimalList = animals.computeIfAbsent(newPosition, k -> new ArrayList<>());
        newAnimalList.add(animal);
    }

    public Vector2d fixPosition(Vector2d position) {
        return position;
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

    @Override
    public void endDay() {
        generatePlants(getMapSpec().dailyPlantsGrowth());
        mapStats.updateStats(this, aliveAnimals, deadAnimals, plants, animals);
        mapChanged("End day");
    }

    @Override
    public void sortAnimals() {
        Random random = new Random();

        Comparator<Animal> animalComparator = Comparator.comparingInt(Animal::getEnergy).reversed().
                thenComparingInt(Animal::getAge).reversed().
                thenComparingInt(Animal::getChildrenCount).reversed().
                thenComparing((a1, a2) -> random.nextBoolean() ? -1 : 1);

        for(Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            List<Animal> animalList = entry.getValue();
            animalList.sort(animalComparator);
        }
    }

    @Override
    public void feedAnimals() {
        for(Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animalList = entry.getValue();

            if(plants.containsKey(position)) {
                Animal animal = animalList.get(0);
                animal.eat();
                plants.remove(position);
            }
        }
    }

    @Override
    public void cleanDeadBodies() {
        synchronized(animals) {
            Iterator<Map.Entry<Vector2d, List<Animal>>> iterator = animals.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<Vector2d, List<Animal>> entry = iterator.next();
                List<Animal> animalList = entry.getValue();
                List<Animal> deadAnimalsOnField = new ArrayList<>();

                for(Animal animal : animalList) {
                    if(animal.isDead()) {
                        deadAnimalsOnField.add(animal);
                        deadAnimals.add(animal);
                        aliveAnimals.remove(animal);
                    }
                }
                animalList.removeAll(deadAnimalsOnField);
                if(animalList.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void reproduceAnimals() {
        for(Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            List<Animal> animalList = entry.getValue();
            if(animalList.size() >= 2) {
                Animal animal1 = animalList.get(0);
                Animal animal2 = animalList.get(1);
                if(animal1.canReproduce() && animal2.canReproduce()) {
                    Animal child = animal1.reproduce(animal2);
                    animalList.add(child);
                    aliveAnimals.add(child);
                }
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return getAnimal(position) != null;
    }

    @Override
    public List<Animal> getAnimal(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public Grass getPlant(Vector2d position) {
        return plants.get(position);
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

    @Override
    public List<Vector2d> getAnimalPositions() {
        return new ArrayList<>(animals.keySet());
    }

    @Override
    public List<Vector2d> getPlantPositions() {
        return new ArrayList<>(plants.keySet());
    }

    @Override
    public boolean isWater(Vector2d position) {
        return false;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(new Vector2d(mapSpec.bounds().getWidth()-1, mapSpec.bounds().getHeight()-1)) &&
                position.follows(new Vector2d(0,0));
    }

    @Override
    public MapSpecification getMapSpec() {
        return this.mapSpec;
    }

    @Override
    public int getWidth() {
        return this.mapSpec.bounds().getWidth();
    }

    @Override
    public int getHeight() {
        return this.mapSpec.bounds().getHeight();
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

    @Override
    public MapStatistics getMapStats() {
        return this.mapStats;
    }
}
