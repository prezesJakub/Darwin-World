package objects;

import information.AnimalSpecification;
import information.GenomeSpecification;
import information.MapSpecification;
import maps.AbstractWorldMap;
import maps.Earth;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void isAtTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(20, 10,
                10, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);
        Animal animal2 = new Animal(new Vector2d(5, 4), animalSpec);

        assertTrue(animal1.isAt(new Vector2d(2, 2)));
        assertTrue(animal2.isAt(new Vector2d(5, 4)));
        assertFalse(animal2.isAt(new Vector2d(2, 2)));
    }

    @Test
    void eatTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(20, 10,
                10, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);

        assertEquals(20, animal1.getEnergy());
        assertEquals(0, animal1.getPlantsEatenCount());
        animal1.eat();

        assertEquals(30, animal1.getEnergy());
        assertEquals(1, animal1.getPlantsEatenCount());
    }

    @Test
    void canReproduceTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec1 = new AnimalSpecification(20, 10,
                25, 10, genomeSpec);
        AnimalSpecification animalSpec2 = new AnimalSpecification(20, 10,
                15, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec1);
        Animal animal2 = new Animal(new Vector2d(2, 2), animalSpec2);

        assertFalse(animal1.canReproduce());
        assertTrue(animal2.canReproduce());
    }

    @Test
    void nextDayTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(20, 10,
                10, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);

        assertEquals(20, animal1.getEnergy());
        assertEquals(0, animal1.getAge());
        assertEquals(0, animal1.getActiveGeneId());

        animal1.nextDay();

        assertEquals(19, animal1.getEnergy());
        assertEquals(1, animal1.getAge());
        assertEquals(1, animal1.getActiveGeneId());
    }

    @Test
    void isDeadTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(1, 10,
                10, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);
        Animal animal2 = new Animal(new Vector2d(3, 3), animalSpec);

        animal2.nextDay();

        assertFalse(animal1.isDead());
        assertTrue(animal2.isDead());
    }

    @Test
    void reproduceTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(20, 10,
                10, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);
        Animal animal2 = new Animal(new Vector2d(2, 2), animalSpec);
        Animal child = animal1.reproduce(animal2);

        assertEquals(1, animal1.getChildrenCount());
        assertEquals(1, animal2.getChildrenCount());
        assertEquals(1, animal1.getDescendantsCount());
        assertEquals(1, animal2.getDescendantsCount());
        assertEquals(10, animal1.getEnergy());
        assertEquals(10, animal2.getEnergy());
        assertEquals(20, child.getEnergy());
    }

    @Test
    void rotateTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(20, 10,
                10, 10, genomeSpec);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);
        MapDirection orientation = animal1.getOrientation();

        animal1.rotate(MapDirection.fromInt(3));

        assertEquals((orientation.toInt()+3)%8, animal1.getOrientation().toInt());
    }

    @Test
    void moveTest() {
        GenomeSpecification genomeSpec = new GenomeSpecification(0, 0, 6,
                MutationType.COMPLETE_RANDOMNESS);
        AnimalSpecification animalSpec = new AnimalSpecification(20, 10,
                10, 10, genomeSpec);
        MapSpecification mapSpec = new MapSpecification(new Boundary(10, 10), 10,
                10, 10, MapType.EARTH);
        Animal animal1 = new Animal(new Vector2d(2, 2), animalSpec);
        AbstractWorldMap map = new Earth(mapSpec);
        MapDirection orientation = animal1.getOrientation();
        Vector2d move = orientation.toUnitVector();

        animal1.move(animal1.getOrientation(), map);

        assertEquals(new Vector2d(2, 2).add(move), animal1.getPosition());
    }
}