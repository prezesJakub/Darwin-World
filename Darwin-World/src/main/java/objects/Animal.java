package objects;

import model.MapDirection;
import model.MapElement;
import model.Vector2d;

public class Animal implements MapElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private int age;

    public Animal(MapDirection orientation, Vector2d position, int energy) {
        this.orientation = orientation;
        this.position = position;
        this.energy = energy;
        this.age = 0;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
    public MapDirection getOrientation() {
        return this.orientation;
    }

    //to do
}
