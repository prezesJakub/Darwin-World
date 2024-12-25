package maps;

import information.MapSpecification;
import information.WaterSpecification;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import model.Vector2d;
import objects.Animal;
import objects.Water;

import java.util.*;

public class WaterMap extends AbstractWorldMap {
    protected final Map<Vector2d, Water> waterMap = Collections.synchronizedMap(new HashMap<>());
    private final WaterSpecification waterSpec;

    public WaterMap(MapSpecification mapSpec, WaterSpecification waterSpec) {
        super(mapSpec);
        this.waterSpec = waterSpec;
    }

    @Override
    public void endDay() {
        generatePlants(getMapSpec().dailyPlantsGrowth());
        changeWaterStates();
        drownAnimals();
        mapChanged("End day");
    }

    public void generateWater(int numberOfWater, int maxRange) {
        Set<Vector2d> waterPositions = new HashSet<>();
        numberOfWater = Math.min(numberOfWater, this.getHeight()*this.getWidth());

        while(numberOfWater > waterPositions.size()) {
            Vector2d position = new Vector2d((int) (Math.random() * (this.getMapSpec().bounds().getWidth())),
                    (int) (Math.random() * (this.getMapSpec().bounds().getHeight())));
            waterPositions.add(position);
        }
        for(Vector2d position : waterPositions) {
            Water water = new Water(position, maxRange);
            waterMap.put(position, water);
        }
    }

    public void changeWaterStates() {
        for(Map.Entry<Vector2d, Water> entry : waterMap.entrySet()) {
            Water water = entry.getValue();
            water.changeWaterState();
        }
    }
    private void drownAnimals() {
        synchronized(animals) {
            Iterator<Map.Entry<Vector2d, List<Animal>>> iterator = animals.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<Vector2d, List<Animal>> entry = iterator.next();
                Vector2d position = entry.getKey();
                List<Animal> animalList = entry.getValue();
                List<Animal> deadAnimalsOnField = new ArrayList<>();

                for(Animal animal : animalList) {
                    if(isWater(position)) {
                        deadAnimalsOnField.add(animal);
                        deadAnimals.add(animal);
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
    public boolean isWater(Vector2d position) {
        int x = position.getX();
        int y = position.getY();
        for(Map.Entry<Vector2d, Water> entry : waterMap.entrySet()) {
            int waterX = entry.getKey().getX();
            int waterY = entry.getKey().getY();
            int actualState = entry.getValue().getActualState();
            if(waterX-actualState <= x && waterX+actualState >= x && waterY-actualState <= y && waterY+actualState >= y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        MapSpecification mapSpec = this.getMapSpec();
        return position.precedes(new Vector2d(mapSpec.bounds().getWidth()-1, mapSpec.bounds().getHeight()-1)) &&
                position.follows(new Vector2d(0,0)) && !isWater(position);
    }

    public WaterSpecification getWaterSpec() {
        return this.waterSpec;
    }
    public List<Vector2d> getWaterPositions() {
        return new ArrayList<>(waterMap.keySet());
    }

}
