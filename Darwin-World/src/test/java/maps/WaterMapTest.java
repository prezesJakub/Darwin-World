package maps;

import information.MapSpecification;
import information.WaterSpecification;
import model.Boundary;
import model.MapType;
import model.Vector2d;
import objects.Water;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaterMapTest {
    @Test
    void isWaterTest() {
        MapSpecification mapSpec = new MapSpecification(new Boundary(10, 10),
                10, 10, 10, MapType.WATER_MAP);
        WaterSpecification waterSpec = new WaterSpecification(1, 4);
        WaterMap map = new WaterMap(mapSpec, waterSpec);
        map.generateWater(1, 4);

        List<Vector2d> waterPositions = map.getWaterPositions();
        Vector2d position2 = waterPositions.get(0).add(new Vector2d(1, 0));

        assertTrue(map.isWater(waterPositions.get(0)));
        assertFalse(map.isWater(position2));
    }

    @Test
    void canMoveToTest() {
        MapSpecification mapSpec = new MapSpecification(new Boundary(10, 10),
                10, 10, 10, MapType.WATER_MAP);
        WaterSpecification waterSpec = new WaterSpecification(1, 4);
        WaterMap map = new WaterMap(mapSpec, waterSpec);
        map.generateWater(1, 4);

        List<Vector2d> waterPositions = map.getWaterPositions();
        Vector2d position2 = waterPositions.get(0).add(new Vector2d(1, 0));

        assertFalse(map.canMoveTo(waterPositions.get(0)));
        assertTrue(map.canMoveTo(position2));
        assertFalse(map.canMoveTo(new Vector2d(10, 10)));
    }
}