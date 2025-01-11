package maps;

import information.MapSpecification;
import model.Boundary;
import model.MapType;
import model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EarthTest {
    @Test
    void canMoveToTest() {
        MapSpecification mapSpec = new MapSpecification(new Boundary(10, 10),
                10, 10, 10, MapType.EARTH);
        Earth map = new Earth(mapSpec);

        assertTrue(map.canMoveTo(new Vector2d(2, 2)));
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertTrue(map.canMoveTo(new Vector2d(9, 9)));
        assertTrue(map.canMoveTo(new Vector2d(10, 2)));
        assertFalse(map.canMoveTo(new Vector2d(2, 10)));
    }
}