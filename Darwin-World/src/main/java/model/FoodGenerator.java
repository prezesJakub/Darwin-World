package model;

import objects.Grass;

import java.util.*;

public class FoodGenerator {
    private Boundary bounds;

    public FoodGenerator(Boundary bounds) {
        this.bounds = bounds;
    }

    public List<Vector2d> generateFood(Map<Vector2d, Grass> foodPositions, int plantsToGrow) {
        int equatorPlants = (int) (0.8 * plantsToGrow);
        int restOfPlants = plantsToGrow - equatorPlants;

        List<Vector2d> newEquatorPlants = new ArrayList<>();
        List<Vector2d> newRestOfPlants = new ArrayList<>();

        segregateFields(foodPositions, newEquatorPlants, newRestOfPlants);

        if(newEquatorPlants.size() < equatorPlants) {
            restOfPlants += (equatorPlants - newEquatorPlants.size());
            equatorPlants = newEquatorPlants.size();
        }
        if(newRestOfPlants.size() < restOfPlants) {
            restOfPlants = newRestOfPlants.size();
        }

        List<Vector2d> newFoodPositions = new ArrayList<>();
        growPlants(equatorPlants, newEquatorPlants, newFoodPositions);
        growPlants(restOfPlants, newRestOfPlants, newFoodPositions);

        return newFoodPositions;
    }
    private void segregateFields(Map<Vector2d, Grass> foodPositions, List<Vector2d> newEquatorPlants,
                                 List<Vector2d> newRestOfPlants) {
        for(int i=0; i<bounds.getWidth(); i++) {
            for(int j=0; j<bounds.getHeight(); j++) {
                Vector2d field = new Vector2d(i, j);
                if(!foodPositions.containsKey(field)) {
                    if(j>=bounds.getLowerEquatorBound() && j<=bounds.getUpperEquatorBound()) {
                        newEquatorPlants.add(field);
                    }
                    else {
                        newRestOfPlants.add(field);
                    }
                }
            }
        }
    }
    private void growPlants(int plantsToGrow, List<Vector2d> possibleFields, List<Vector2d> newFoodPositions) {
        Collections.shuffle(possibleFields);
        for(int i=0; i<plantsToGrow; i++) {
            Vector2d field = possibleFields.get(i);
            newFoodPositions.add(field);
        }
    }
}
